package com.spacetime.evcharging.feature.station.discovery

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.spacetime.evcharging.data.model.Station

@Composable
fun GoogleMapsView(
    stations: List<Station>,
    selectedStation: Station?,
    userLatitude: Double,
    userLongitude: Double,
    onSelectStation: (Station) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialPos = remember(userLatitude, userLongitude) {
        LatLng(userLatitude, userLongitude)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 13f)
    }

    LaunchedEffect(selectedStation) {
        selectedStation?.let { s ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(s.latitude, s.longitude), 15f),
                durationMs = 800
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
        MapProperties(
            isMyLocationEnabled = false
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        // User Location Marker
        Marker(
            state = MarkerState(position = LatLng(userLatitude, userLongitude)),
            title = "My Location",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )

        // Station Markers
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
