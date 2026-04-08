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
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme


@Composable
fun MainScreen(
    onOpenProfile: () -> Unit,
    onOpenPlanTrip: () -> Unit,
    onOpenHistory: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

       // Map placeholder image
        Image(
            painter = painterResource(id = R.drawable.map_placeholder),
            contentDescription = "Map placeholder",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Profile Button
        Button(
            onClick = onOpenProfile,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(2, 38, 88))
        ) {
            Text("Profile")
        }

        // Bottom bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
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
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88))
                ) {
                    Text("Plan Trip")
                }

                Button(
                    onClick = onOpenHistory,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88))
                ) {
                    Text("History")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TravelTrakerappTheme {
        MainScreen(onOpenProfile = {}, onOpenPlanTrip = {}, onOpenHistory = {})
    }
}