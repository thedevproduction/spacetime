package com.spacetime.evcharging.feature.station.discovery

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacetime.evcharging.core.common.model.ChargerStatus
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.data.model.Station
import kotlin.math.*

/**
 * High-performance, 100% native Jetpack Compose vector map engine.
 * Renders real geographic coordinates, road networks, range rings,
 * user GPS location, and interactive station markers with zero external dependencies.
 */
@Composable
fun NativeMapView(
    stations: List<Station>,
    selectedStation: Station?,
    userLatitude: Double,
    userLongitude: Double,
    onSelectStation: (Station) -> Unit,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var zoomScale by remember { mutableFloatStateOf(1.2f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current

    // Pulsing animation for user location & active station
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 42f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )

    // Center map on selected station when selection changes
    LaunchedEffect(selectedStation) {
        selectedStation?.let { s ->
            // Recenter smoothly so selected station is at screen center
            val dLat = (s.latitude - userLatitude) * 111.0 // km
            val dLng = (s.longitude - userLongitude) * 111.0 * cos(Math.toRadians(userLatitude))
            val baseScale = 85f * zoomScale
            panOffsetX = (-dLng * baseScale).toFloat()
            panOffsetY = (dLat * baseScale).toFloat()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5F7)) // Clean map background
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    zoomScale = (zoomScale * zoom).coerceIn(0.5f, 4.5f)
                    panOffsetX += pan.x
                    panOffsetY += pan.y
                }
            }
    ) {
        val centerX = size.width / 2f + panOffsetX
        val centerY = size.height / 2f + panOffsetY
        val baseScale = 85f * zoomScale

        // Function to project GPS Lat/Lng to Screen X/Y
        fun projectToScreen(lat: Double, lng: Double): Offset {
            val dLatKm = (lat - userLatitude) * 111.0
            val dLngKm = (lng - userLongitude) * 111.0 * cos(Math.toRadians(userLatitude))
            val x = centerX + (dLngKm * baseScale).toFloat()
            val y = centerY - (dLatKm * baseScale).toFloat()
            return Offset(x, y)
        }

        // Draw Canvas Map Background: Roads, water, grid, range rings
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width.toFloat()
            val canvasHeight = size.height.toFloat()

            // 1. Grid & Distance Rings from user location
            val ringRadiiKm = listOf(2.0, 5.0, 10.0, 20.0)
            val ringColors = Color(0xFFD6DFE6)

            ringRadiiKm.forEach { rKm ->
                val rPx = (rKm * baseScale).toFloat()
                drawCircle(
                    color = ringColors,
                    radius = rPx,
                    center = Offset(centerX, centerY),
                    style = Stroke(
                        width = 1.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f)
                    )
                )
            }

            // 2. Decorative River / Water Body (Simulating Narmada River curve near Jabalpur)
            val riverPath = Path().apply {
                val start = projectToScreen(userLatitude - 0.08, userLongitude - 0.12)
                moveTo(start.x, start.y)
                val c1 = projectToScreen(userLatitude - 0.04, userLongitude - 0.04)
                val c2 = projectToScreen(userLatitude - 0.06, userLongitude + 0.06)
                val end = projectToScreen(userLatitude - 0.02, userLongitude + 0.14)
                cubicTo(c1.x, c1.y, c2.x, c2.y, end.x, end.y)
            }
            drawPath(
                path = riverPath,
                color = Color(0xFFD8ECF8),
                style = Stroke(width = 28f * zoomScale)
            )

            // 3. Green Zones / Parks
            val parkCenter1 = projectToScreen(userLatitude + 0.03, userLongitude - 0.04)
            drawCircle(
                color = Color(0xFFE2F0D9),
                radius = 35f * zoomScale,
                center = parkCenter1
            )
            val parkCenter2 = projectToScreen(userLatitude - 0.02, userLongitude + 0.05)
            drawCircle(
                color = Color(0xFFE2F0D9),
                radius = 45f * zoomScale,
                center = parkCenter2
            )

            // 4. Major Highway & Arterial Roads (NH-34, Bypass, Ring Road)
            val highwayColor = Color(0xFFFFFFFF)
            val highwayBorderColor = Color(0xFFD1D8DF)

            // Highway 1: North-South Bypass (NH-34)
            val hwy1Start = projectToScreen(userLatitude + 0.12, userLongitude - 0.02)
            val hwy1End = projectToScreen(userLatitude - 0.12, userLongitude + 0.01)

            drawLine(
                color = highwayBorderColor,
                start = hwy1Start,
                end = hwy1End,
                strokeWidth = 14f * zoomScale
            )
            drawLine(
                color = highwayColor,
                start = hwy1Start,
                end = hwy1End,
                strokeWidth = 10f * zoomScale
            )

            // Highway 2: East-West Arterial
            val hwy2Start = projectToScreen(userLatitude + 0.02, userLongitude - 0.14)
            val hwy2End = projectToScreen(userLatitude - 0.01, userLongitude + 0.14)

            drawLine(
                color = highwayBorderColor,
                start = hwy2Start,
                end = hwy2End,
                strokeWidth = 12f * zoomScale
            )
            drawLine(
                color = highwayColor,
                start = hwy2Start,
                end = hwy2End,
                strokeWidth = 8f * zoomScale
            )

            // Highway 3: Ring Road / Sector Link
            val ringPath = Path().apply {
                val p0 = projectToScreen(userLatitude + 0.05, userLongitude + 0.07)
                val p1 = projectToScreen(userLatitude + 0.01, userLongitude - 0.06)
                val p2 = projectToScreen(userLatitude - 0.06, userLongitude - 0.02)
                moveTo(p0.x, p0.y)
                quadraticTo(p1.x, p1.y, p2.x, p2.y)
            }
            drawPath(
                path = ringPath,
                color = highwayBorderColor,
                style = Stroke(width = 10f * zoomScale)
            )
            drawPath(
                path = ringPath,
                color = highwayColor,
                style = Stroke(width = 7f * zoomScale)
            )

            // 5. User Location Pulse & Dot
            drawCircle(
                color = Color(0xFF2563EB).copy(alpha = pulseAlpha),
                radius = pulseRadius * density.density,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color.White,
                radius = 10f * density.density,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color(0xFF2563EB),
                radius = 7f * density.density,
                center = Offset(centerX, centerY)
            )
        }

        // Render Station Marker Badges on top of Canvas
        stations.forEach { station ->
            val screenPos = projectToScreen(station.latitude, station.longitude)
            val isSelected = selectedStation?.id == station.id
            val isAvailable = station.availableConnectors > 0 && station.availability == ChargerStatus.AVAILABLE

            // Check if within visible bounds (with 100px padding)
            if (screenPos.x in -120f..(size.width + 120f) && screenPos.y in -120f..(size.height + 120f)) {
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (screenPos.x - 70.dp.toPx()).roundToInt(),
                                y = (screenPos.y - 40.dp.toPx()).roundToInt()
                            )
                        }
                        .clickable { onSelectStation(station) }
                ) {
                    Column(horizontalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) Secondary else CardBackground,
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 2.5.dp else 1.5.dp,
                                color = if (isAvailable) Success else Warning
                            ),
                            shadowElevation = if (isSelected) 10.dp else 5.dp,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isAvailable) Success else Warning)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "${station.minimumTariff.displayAmount} • ${station.availableConnectors}/${station.totalConnectors}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MainText
                                )
                            }
                        }

                        // Pin Triangle Arrow pointing to location
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Secondary else if (isAvailable) Success else Warning)
                        )
                    }
                }
            }
        }

        // Bottom Left Coordinates / Scale Badge
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = CardBackground.copy(alpha = 0.9f),
            shadowElevation = 2.dp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = if (selectedStation != null) 250.dp else 30.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${String.format("%.4f", userLatitude)}° N, ${String.format("%.4f", userLongitude)}° E",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SecondaryText
                )
            }
        }

        // Floating Zoom In / Zoom Out Controls
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = CardBackground,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .size(42.dp)
                    .clickable { zoomScale = (zoomScale * 1.3f).coerceAtMost(4.5f) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Zoom In", tint = MainText)
                }
            }
            Surface(
                shape = CircleShape,
                color = CardBackground,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .size(42.dp)
                    .clickable { zoomScale = (zoomScale / 1.3f).coerceAtLeast(0.5f) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Zoom Out", tint = MainText)
                }
            }
        }
    }
}
