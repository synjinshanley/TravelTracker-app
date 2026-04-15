package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme

@Composable
fun PlanTripScreen(modifier: Modifier = Modifier, viewModel: MapViewModel, onBack: () -> Unit) {
    var startingLocation by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(color = Color.LightGray), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier.fillMaxWidth().padding(top = 15.dp, start = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(2, 38, 88))) {

                Text("Return")
            }
        }
        Box(Modifier.fillMaxWidth().padding(10.dp).background(Color(0xFF2C2C2E))) {
            Column() {
                Row() {
                    Text("Where are we starting?", Modifier.padding(10.dp), color = Color.White)
                }
                Row() {
                    OutlinedTextField(
                        modifier = Modifier.background(Color.White).fillMaxWidth(),
                        value = startingLocation,
                        onValueChange = { startingLocation = it },
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth().padding(10.dp).background(Color(0xFF2C2C2E))) {
            Column() {
                Row() {
                    Text("Where are we going? (address, city name, etc.)", Modifier.padding(10.dp), color = Color.White)
                }
                Row() {
                    OutlinedTextField(
                        modifier = Modifier.background(Color.White).fillMaxWidth(),
                        value = startingLocation,
                        onValueChange = { startingLocation = it },
                    )
                }
                Row() {
                    Text("How will we get there?", Modifier.padding(10.dp), color = Color.White)
                }
                Row() {
                    OutlinedTextField(
                        modifier = Modifier.background(Color.White).fillMaxWidth(),
                        value = startingLocation,
                        onValueChange = { startingLocation = it },
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth().padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88))) {
                    Text("Add Stop")
                }
                Button(
                    onClick = {
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88))) {

                    Text("Finalize")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanTripScreenPreview() {
    TravelTrakerappTheme() {
        //PlanTripScreen()
    }
}
