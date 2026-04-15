package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import edu.gvsu.cis.traveltracker_app.R
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState




@Composable
fun MapScreen() {
    val location = LatLng(51.5074, -0.1278) // Example: London
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = location),
            title = "My Marker"
        )
    }
}

@Composable
fun MainScreen(
    onOpenProfile: () -> Unit,
    onOpenPlanTrip: () -> Unit,    onOpenHistory: () -> Unit
) {
    androidx.compose.material3.Scaffold(
        bottomBar = {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray
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
    ) { innerPadding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            MapScreen()


            Button(
                onClick = onOpenProfile,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(2, 38, 88))
            ) {
                Text("Profile")
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