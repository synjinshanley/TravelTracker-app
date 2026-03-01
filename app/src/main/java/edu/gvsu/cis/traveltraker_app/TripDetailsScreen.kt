package edu.gvsu.cis.traveltraker_app

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.gvsu.cis.traveltraker_app.ui.theme.TravelTrakerappTheme

@Composable
fun TripDetailsScreen() {
    Column {
        Button(
            onClick = {

            }) {
            Text("Return")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripDetailsScreenPreview() {
    TravelTrakerappTheme() {
        TripDetailsScreen()
    }
}