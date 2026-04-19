package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import edu.gvsu.cis.traveltracker_app.R
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Polyline
import androidx.compose.runtime.LaunchedEffect
import edu.gvsu.cis.traveltracker_app.TravelViewModel


@Composable
fun MapScreen(locations: List<TripStop>) {
    val defaultPosition = LatLng(51.5074, -0.1278)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultPosition, 5f)
    }
    val tripColors = listOf(
        Color(2, 38, 88),
        Color(180, 30, 30),
        Color(20, 120, 50),
        Color(150, 80, 180),
        Color(200, 120, 0)
    )

    fun colorForTrip(tripIndex: Int) = tripColors[tripIndex % tripColors.size]

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        locations.forEach { location ->
            Marker(
                state = MarkerState(position = LatLng(location.stopLatLng.latitude, location.stopLatLng.longitude)),
                title = location.location,
                snippet = location.notes.ifEmpty { location.transportation }
            )
        }
        if (locations.isNotEmpty()) {
            // Group by tripIndex and draw each trip's route separately
            locations.groupBy { it.tripIndex }.forEach { (tripIndex, tripStops) ->
                val color = colorForTrip(tripIndex)
                tripStops.zipWithNext { a, b ->
                    Polyline(
                        points = listOf(a.stopLatLng, b.stopLatLng),
                        color = color,
                        width = 8f,
                        geodesic = true
                    )
                }
            }
        }
    }
}
@Composable
fun MainScreen(
    viewModel: TravelViewModel,
    onOpenProfile: () -> Unit,
    onOpenPlanTrip: () -> Unit,
    onOpenHistory: () -> Unit
) {
    val trips by viewModel.trips.collectAsState()
    val locations by viewModel.allStops.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTrips()
        viewModel.loadAllStops()
    }

            Box(modifier = Modifier.fillMaxSize()) {
        MapScreen(locations)

        // Profile Button
        Button(
            onClick = onOpenProfile,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(2, 38, 88))
        ) {
            Text("Profile")
        }

        // Bottom bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onOpenPlanTrip,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(2, 38, 88))
                ) {
                    Text("Plan Trip")
                }

                Button(
                    onClick = onOpenHistory,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(2, 38, 88))
                ) {
                    Text("History")
                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TravelTrackerappTheme {
        MainScreen(onOpenProfile = {}, onOpenPlanTrip = {}, onOpenHistory = {})
    }
}*/