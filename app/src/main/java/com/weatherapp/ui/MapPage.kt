package com.weatherapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.weatherapp.MainViewModel
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MapPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {

    val context = LocalContext.current
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val camPosState = rememberCameraPositionState()

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = camPosState,
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = hasLocationPermission),
        onMapClick = { latLng ->
            viewModel.add(
                name = "Cidade@${latLng.latitude}:${latLng.longitude}",
                location = latLng
            )
        }
    ) {
        viewModel.cities.forEach { city ->
            if (city.location != null) {
                Marker(
                    state = MarkerState(position = city.location),
                    title = city.name,
                    snippet = "Lat: ${city.location.latitude}, Lng: ${city.location.longitude}"
                )
            }
        }
    }
}