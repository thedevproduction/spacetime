package com.spacetime.evcharging.feature.station.discovery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.spacetime.evcharging.core.common.model.ChargerStatus
import com.spacetime.evcharging.core.designsystem.theme.Border
import com.spacetime.evcharging.core.designsystem.theme.CardBackground
import com.spacetime.evcharging.core.designsystem.theme.MainText
import com.spacetime.evcharging.core.designsystem.theme.Primary
import com.spacetime.evcharging.core.designsystem.theme.SecondaryText
import com.spacetime.evcharging.data.model.Station
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.io.File
import android.graphics.Color as AndroidColor

private val CartoVoyagerTileSource = XYTileSource(
    "CartoVoyager",
    1,
    19,
    256,
    ".png",
    arrayOf(
        "https://a.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://b.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://c.basemaps.cartocdn.com/rastertiles/voyager/",
        "https://d.basemaps.cartocdn.com/rastertiles/voyager/"
    )
)

/**
 * Real OpenStreetMap native Android MapView (via osmdroid) displaying authentic
 * street networks, roads, neighborhoods, buildings, and interactive EV charging pins.
 */
@Composable
fun OsmRealMapView(
    stations: List<Station>,
    selectedStation: Station?,
    userLatitude: Double,
    userLongitude: Double,
    recenterTrigger: Int,
    useVoyagerStyle: Boolean = false,
    onSelectStation: (Station?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current.density

    // Configure osmdroid cache & User-Agent BEFORE creating MapView
    val mapView = remember {
        val appContext = context.applicationContext
        Configuration.getInstance().apply {
            load(appContext, appContext.getSharedPreferences("osmdroid_prefs", Context.MODE_PRIVATE))
            userAgentValue = "SpacetimeEVCharging/1.0 (${appContext.packageName})"
            osmdroidBasePath = File(appContext.cacheDir, "osmdroid").apply { mkdirs() }
            osmdroidTileCache = File(appContext.cacheDir, "osmdroid/tiles").apply { mkdirs() }
        }
        MapView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setTileSource(if (useVoyagerStyle) CartoVoyagerTileSource else TileSourceFactory.MAPNIK)
            isTilesScaledToDpi = true
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            minZoomLevel = 4.0
            maxZoomLevel = 19.5
            controller.setZoom(14.0)
            controller.setCenter(GeoPoint(userLatitude, userLongitude))
        }
    }

    LaunchedEffect(useVoyagerStyle) {
        mapView.setTileSource(if (useVoyagerStyle) CartoVoyagerTileSource else TileSourceFactory.MAPNIK)
        mapView.invalidate()
    }

    // Bind MapView lifecycle cleanly
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onPause()
            mapView.onDetach()
        }
    }

    // Center camera when user coordinates change or recenter button pressed
    LaunchedEffect(userLatitude, userLongitude, recenterTrigger) {
        mapView.controller.animateTo(GeoPoint(userLatitude, userLongitude), 14.0, 600L)
    }

    // Center camera on selected station
    LaunchedEffect(selectedStation?.id) {
        selectedStation?.let { s ->
            mapView.controller.animateTo(GeoPoint(s.latitude, s.longitude), 15.2, 550L)
        }
    }

    // Update overlays whenever stations, selectedStation, or user location change
    LaunchedEffect(stations, selectedStation?.id, userLatitude, userLongitude) {
        mapView.overlays.clear()

        // Tap anywhere on empty map to dismiss selection card
        val tapOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                onSelectStation(null)
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean = false
        })
        mapView.overlays.add(tapOverlay)

        // 1. User GPS location marker
        val userMarker = Marker(mapView).apply {
            position = GeoPoint(userLatitude, userLongitude)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            title = "Your Location"
            icon = BitmapDrawable(context.resources, createUserLocationBitmap(density))
            setOnMarkerClickListener { _, _ -> true }
        }
        mapView.overlays.add(userMarker)

        // 2. Charging Station Markers
        stations.forEach { station ->
            val isSelected = selectedStation?.id == station.id
            val isAvailable = station.availableConnectors > 0 && station.availability == ChargerStatus.AVAILABLE
            val compactPrice = station.minimumTariff.displayAmount.removeSuffix(".00")
            val badgeText = "$compactPrice • ${station.availableConnectors}/${station.totalConnectors}"

            val stationMarker = Marker(mapView).apply {
                position = GeoPoint(station.latitude, station.longitude)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = station.name
                snippet = "${station.distanceKm} km • ${station.availableConnectors}/${station.totalConnectors} available"
                icon = BitmapDrawable(
                    context.resources,
                    createStationMarkerBitmap(
                        text = badgeText,
                        isAvailable = isAvailable,
                        isSelected = isSelected,
                        density = density
                    )
                )
                setOnMarkerClickListener { _, _ ->
                    onSelectStation(station)
                    true
                }
            }
            mapView.overlays.add(stationMarker)
        }

        mapView.invalidate()
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Grouped Zoom Controls (+/-) positioned cleanly below top chips
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = CardBackground.copy(alpha = 0.96f),
            border = BorderStroke(1.dp, Border),
            shadowElevation = 4.dp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 132.dp, end = 16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { mapView.controller.zoomIn() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Zoom In",
                        tint = MainText,
                        modifier = Modifier.size(20.dp)
                    )
                }
                HorizontalDivider(
                    color = Border,
                    thickness = 0.75.dp,
                    modifier = Modifier.width(26.dp)
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .clickable { mapView.controller.zoomOut() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Zoom Out",
                        tint = MainText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Clean OSM attribution badge
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = CardBackground.copy(alpha = 0.92f),
            border = BorderStroke(0.5.dp, Border),
            shadowElevation = 2.dp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = if (selectedStation != null) 256.dp else 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "© OpenStreetMap",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = SecondaryText
                )
            }
        }
    }
}

private fun createUserLocationBitmap(density: Float): Bitmap {
    val sizePx = (44f * density).toInt().coerceAtLeast(32)
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val cx = sizePx / 2f
    val cy = sizePx / 2f

    // Outer halo
    val haloPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.argb(60, 37, 99, 235)
        style = Paint.Style.FILL
    }
    canvas.drawCircle(cx, cy, sizePx * 0.45f, haloPaint)

    // White ring
    val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(cx, cy, 10f * density, whitePaint)

    // Blue core dot
    val bluePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.rgb(37, 99, 235)
        style = Paint.Style.FILL
    }
    canvas.drawCircle(cx, cy, 7f * density, bluePaint)

    return bitmap
}

private fun createStationMarkerBitmap(
    text: String,
    isAvailable: Boolean,
    isSelected: Boolean,
    density: Float
): Bitmap {
    val textSizePx = 11.5f * density
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isSelected) AndroidColor.WHITE else AndroidColor.rgb(23, 43, 77)
        textSize = textSizePx
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val textWidth = textPaint.measureText(text)
    val dotRadius = 4f * density
    val dotSpace = 6f * density
    val hPad = 10f * density
    val pillWidth = (hPad * 2 + dotRadius * 2 + dotSpace + textWidth).toInt()
    val pillHeight = (28f * density).toInt()
    val pointerHeight = (8f * density).toInt()
    val shadowPad = (4f * density).toInt()

    val totalW = pillWidth + shadowPad * 2
    val totalH = pillHeight + pointerHeight + shadowPad * 2

    val bitmap = Bitmap.createBitmap(totalW, totalH, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val pillLeft = shadowPad.toFloat()
    val pillTop = shadowPad.toFloat()
    val pillRight = pillLeft + pillWidth
    val pillBottom = pillTop + pillHeight
    val rect = RectF(pillLeft, pillTop, pillRight, pillBottom)
    val cornerRadius = pillHeight / 2f

    val statusColor = if (isAvailable) AndroidColor.rgb(22, 163, 74) else AndroidColor.rgb(245, 158, 11)
    val bgColor = if (isSelected) AndroidColor.rgb(16, 42, 67) else AndroidColor.WHITE
    val borderColor = if (isSelected) AndroidColor.rgb(8, 127, 91) else statusColor

    // Drop shadow
    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = AndroidColor.argb(55, 16, 42, 67)
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(
        RectF(pillLeft, pillTop + 2f * density, pillRight, pillBottom + 2f * density),
        cornerRadius,
        cornerRadius,
        shadowPaint
    )

    // Pointer triangle
    val centerX = totalW / 2f
    val pointerPath = Path().apply {
        moveTo(centerX - 5f * density, pillBottom - 2f)
        lineTo(centerX, pillBottom + pointerHeight)
        lineTo(centerX + 5f * density, pillBottom - 2f)
        close()
    }
    val pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.FILL
    }
    canvas.drawPath(pointerPath, pointerPaint)

    // Pill fill
    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = bgColor
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, fillPaint)

    // Pill border
    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = borderColor
        style = Paint.Style.STROKE
        strokeWidth = if (isSelected) 2.5f * density else 1.8f * density
    }
    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, strokePaint)

    // Availability status dot
    val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = statusColor
        style = Paint.Style.FILL
    }
    val dotCx = pillLeft + hPad + dotRadius
    val dotCy = pillTop + pillHeight / 2f
    canvas.drawCircle(dotCx, dotCy, dotRadius, dotPaint)

    // Price & connector count label
    val fm = textPaint.fontMetrics
    val textY = dotCy - (fm.ascent + fm.descent) / 2f
    canvas.drawText(text, dotCx + dotRadius + dotSpace, textY, textPaint)

    return bitmap
}
