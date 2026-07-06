package com.example.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.Court
import com.example.ui.theme.Asphalt
import com.example.ui.theme.BorderGrey
import com.example.ui.theme.NeonGreen
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun OpenStreetMapView(
    courts: List<Court>,
    onCourtClick: (Court) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var mapView by remember { mutableStateOf<MapView?>(null) }
    var hasLocationPermission by remember { mutableStateOf(checkLocationPermission(context)) }
    var locationRequested by remember { mutableStateOf(false) }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (granted) {
            enableMyLocation(mapView)
            mapView?.invalidate()
        } else if (locationRequested) {
            Toast.makeText(
                context,
                "Permiso de ubicación denegado. Activálo desde Configuración para ver canchas cercanas.",
                Toast.LENGTH_LONG
            ).show()
        }
        locationRequested = true
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    hasLocationPermission = checkLocationPermission(context)
                    mapView?.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> mapView?.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView?.onDetach()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Asphalt)
            .border(1.dp, BorderGrey)
    ) {
        AndroidView(
            factory = { ctx ->
                Configuration.getInstance().apply {
                    userAgentValue = ctx.packageName
                    osmdroidTileCache = ctx.cacheDir
                    osmdroidBasePath = ctx.filesDir
                }

                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    val controller = controller
                    controller.setZoom(14.0)
                    controller.setCenter(GeoPoint(-24.7854, -65.4112))
                    setMultiTouchControls(true)
                    isHorizontalMapRepetitionEnabled = false
                    isVerticalMapRepetitionEnabled = false
                    minZoomLevel = 4.0

                    mapView = this
                }
            },
            update = { view ->
                view.overlays.removeAll { it is Marker }
                courts.forEach { court ->
                    if (court.latitude != 0.0 && court.longitude != 0.0) {
                        val marker = Marker(view)
                        marker.position = GeoPoint(court.latitude, court.longitude)
                        marker.title = court.name
                        marker.subDescription = "${court.type} • ${court.surface}"
                        marker.setOnMarkerClickListener { _, _ ->
                            onCourtClick(court)
                            true
                        }
                        view.overlays.add(marker)
                    }
                }
                val hasOverlay = view.overlays.any { it is MyLocationNewOverlay }
                if (hasLocationPermission && !hasOverlay) {
                    enableMyLocation(view)
                } else if (!hasLocationPermission && hasOverlay) {
                    view.overlays.removeAll { it is MyLocationNewOverlay }
                }
                view.invalidate()
            }
        )

        if (!hasLocationPermission) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
                    .background(Color.Black.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, NeonGreen, shape = RoundedCornerShape(8.dp))
                    .clickable {
                        locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "📍 ACTIVÁ UBICACIÓN para ver canchas cercanas",
                    color = NeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun enableMyLocation(mapView: MapView?) {
    if (mapView == null) return
    val hasOverlay = mapView.overlays.any { it is MyLocationNewOverlay }
    if (!hasOverlay) {
        val myLocationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(mapView.context),
            mapView
        )
        myLocationOverlay.enableMyLocation()
        mapView.overlays.add(myLocationOverlay)
    }
}

private fun checkLocationPermission(context: Context): Boolean {
    return androidx.core.app.ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}
