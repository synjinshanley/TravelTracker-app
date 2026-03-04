package edu.gvsu.cis.traveltraker_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val BackgroundDark = Color(0xFF1C1C1E)
val CardDark = Color(0xFF2C2C2E)

data class TripUi(
    val id: String,
    val name: String
)

@Composable
fun HistoryScreen(
    trips: List<TripUi>,
    onOpenTripDetails: (String) -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        Row() {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardDark,
                    contentColor = Color.White
                )
            ) {
                Text("Back")
            }

            Text(
                text = "     Trip History",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 28.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(trips) { trip ->

                TripHistoryItem(
                    trip = trip,
                    onClick = {
                        onOpenTripDetails(trip.id)
                    }
                )
            }
        }
    }
}

@Composable
fun TripHistoryItem(
    trip: TripUi,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        onClick = onClick
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = trip.name,
                color = Color.White
            )
        }
    }
}