package edu.gvsu.cis.traveltracker_app

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme

@Composable

fun TripDetailsScreen(modifier: Modifier = Modifier, tripId: String, onBack: () -> Unit) {
    Column(Modifier
        .fillMaxSize()
        .background(color = Color.LightGray), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    onBack()
                }) {

                Text("Return")
            }
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(2, 38, 88))
            ) {
                Text("Edit")
            }
        }
        Card(Modifier
            .fillMaxSize()
            .padding(15.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(
            containerColor = Color.Gray
        )) {
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(
                            color = Color(2, 38, 88),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "May 2024 Trip Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp,
                                topEnd = 12.dp,
                                bottomEnd = 12.dp
                            )
                        )
                ) {
                    Text(
                        text = "Started in - London, UK",
                        modifier = Modifier.padding(10.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row() {
                    Text("Traveled by Car to - Paris, France", Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
                }
                Box(Modifier.padding(10.dp)) {
                    Column() {
                        Row {
                            Text("Traveled 344 km", Modifier.padding(horizontal = 10.dp))
                        }
                        Row {
                            Text("Visited - The Eifel Tower, the Louvre", Modifier.padding(horizontal = 10.dp))
                        }
                        Row {
                            Text("Notes: Excellent food, tried Escargot for the first time!", Modifier.padding(horizontal = 10.dp))
                        }
                    }
                }
                Row() {
                    Text("Traveled by Plane to - Rome, Italy", Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
                }
                Box(Modifier.padding(10.dp)) {
                    Column() {
                        Row {
                            Text("Traveled 1422 km", Modifier.padding(horizontal = 10.dp))
                        }
                        Row {
                            Text("Visited - The Coliseum, The Pantheon", Modifier.padding(horizontal = 10.dp))
                        }
                        Row {
                            Text("Notes: Amassing scenery, must come back at some point.", Modifier.padding(horizontal = 10.dp))
                        }
                    }
                }
                Row() {
                    Text("Return trip by Plane/car to - London, UK", Modifier.padding(10.dp), fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .background(
                            color = Color(2, 38, 88),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Total Distance Traveled:",
                        color = Color.White
                    )
                }
            }
        }

    }
}
/*
fun travelCard() {
    Row() {
        Text("Traveled by __ to - __, __", Modifier.padding(10.dp))
    }

    Box(Modifier.padding(10.dp)) {
        Column() {
            Row {
                Text("Traveled __ km", Modifier.padding(horizontal = 10.dp))
            }
            Row {
                Text("Visited: ___", Modifier.padding(horizontal = 10.dp))
            }
            Row {
                Text("Notes: ___", Modifier.padding(horizontal = 10.dp))
            }
        }
    }

}
*/
@Preview(showBackground = true)
@Composable
fun TripDetailsScreenPreview() {
    TravelTrakerappTheme() {
        TripDetailsScreen(tripId = "abcd", onBack = {})
    }
}