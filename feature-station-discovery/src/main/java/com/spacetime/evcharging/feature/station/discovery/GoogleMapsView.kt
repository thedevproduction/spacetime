package com.spacetime.evcharging.feature.station.discovery

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.spacetime.evcharging.core.common.model.ChargerStatus
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.data.model.Station

@Composable
fun GoogleMapsView(
    stations: List<Station>,
    selectedStation: Station?,
    userLatitude: Double,
    userLongitude: Double,
    onSelectStation: (Station) -> Unit,
    onFallbackToStreetMap: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hasValidApiKey = remember(context) {
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            val key = appInfo.metaData?.getString("com.google.android.geo.API_KEY")
            !key.isNullOrBlank()
        } catch (_: Exception) {
            false
        }
    }

    if (!hasValidApiKey) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Google Maps API Key Required",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add MAPS_API_KEY=... to gradle.properties to render Google Maps SDK tiles, or switch to free OpenStreetMap street tiles.",
                        fontSize = 13.sp,
                        color = SecondaryText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onFallbackToStreetMap,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Use OpenStreetMap Street View", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
        return
    }

    val initialPos = remember(userLatitude, userLongitude) {
        LatLng(userLatitude, userLongitude)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 13.5f)
    }

    LaunchedEffect(userLatitude, userLongitude) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(LatLng(userLatitude, userLongitude), 14f),
            durationMs = 600
        )
    }

    LaunchedEffect(selectedStation?.id) {
        selectedStation?.let { s ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(s.latitude, s.longitude), 15f),
                durationMs = 700
            )
        }
    }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            compassEnabled = true,
            rotationGesturesEnabled = true,
            scrollGesturesEnabled = true,
            tiltGesturesEnabled = false,
            zoomGesturesEnabled = true
        )
    }

    val properties = remember {
        MapProperties(isMyLocationEnabled = false)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        Marker(
            state = MarkerState(position = LatLng(userLatitude, userLongitude)),
            title = "My Location",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )

        stations.forEach { station ->
            val isAvail = station.availableConnectors > 0 && station.availability == ChargerStatus.AVAILABLE
            val markerHue = if (isAvail) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_ORANGE

            Marker(
                state = MarkerState(position = LatLng(station.latitude, station.longitude)),
                title = station.name,
                snippet = "${station.minimumTariff.displayAmount}/kWh • ${station.availableConnectors}/${station.totalConnectors} available",
                icon = BitmapDescriptorFactory.defaultMarker(markerHue),
                onClick = {
                    onSelectStation(station)
                    false
                }
            )
        }
    }
}
