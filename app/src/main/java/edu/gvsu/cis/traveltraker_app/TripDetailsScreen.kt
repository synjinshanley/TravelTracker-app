package edu.gvsu.cis.traveltraker_app

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TripDetailsScreen(
    tripId: String,
    onBack: () -> Unit
) {

    Column {

        Text("Trip Details Screen")

        Text("Trip ID: $tripId")

        Button(
            onClick = onBack
        ) {
            Text("Back to History")
        }
    }
}