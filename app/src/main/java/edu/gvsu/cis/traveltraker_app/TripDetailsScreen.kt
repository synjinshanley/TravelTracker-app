package edu.gvsu.cis.traveltraker_app

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.gvsu.cis.traveltraker_app.ui.theme.TravelTrakerappTheme

@Composable
fun TripDetailsScreen(modifier: Modifier = Modifier, tripId: String, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(color = Color(red = 175, green = 40, blue = 60, alpha = 255)), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    onBack()
                }) {
                Text("Return")
            }
            Button(
                onClick = {

                }) {
                Text("Edit")
            }
        }
        Box(Modifier.fillMaxSize().padding(10.dp).background(color = Color(40, 120, 240, 255))) {
            Column() {
                Row() {
                    Text("May 2024 Trip Details:", Modifier.padding(10.dp))
                }
                Row() {
                    Text("Started in - London, UK", Modifier.padding(10.dp))
                }
                Row() {
                    Text("Traveled by Car to - Paris, France", Modifier.padding(10.dp))
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
                    Text("Traveled by Plane to - Rome, Italy", Modifier.padding(10.dp))
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
                    Text("Return trip by Plane/car to - London, UK", Modifier.padding(10.dp))
                }
                Row() {
                    Text("Total distance traveled: 3188 km", Modifier.padding(10.dp))
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TripDetailsScreenPreview() {
    TravelTrakerappTheme() {
        //TripDetailsScreen(tripID = "abcd")
    }
}