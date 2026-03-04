package edu.gvsu.cis.traveltraker_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import edu.gvsu.cis.traveltraker_app.ui.theme.TravelTrakerappTheme

@Composable
fun PlanTripScreen(modifier: Modifier = Modifier) {
    var startingLocation by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(color = Color(red = 175, green = 40, blue = 60, alpha = 255)), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier.fillMaxWidth().padding(horizontal = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {

                }) {
                Text("Return")
            }
            Button(
                onClick = {

                }) {
                Text("Edit")
            }
        }
        Box(Modifier.fillMaxWidth().padding(10.dp).background(color = Color(40, 120, 240, 255))) {
            Column() {
                Row() {
                    Text("Where are we starting?", Modifier.padding(10.dp))
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
        Box(Modifier.fillMaxWidth().padding(10.dp).background(color = Color(40, 120, 240, 255))) {
            Column() {
                Row() {
                    Text("Where are we going?", Modifier.padding(10.dp))
                }
                Row() {
                    OutlinedTextField(
                        modifier = Modifier.background(Color.White).fillMaxWidth(),
                        value = startingLocation,
                        onValueChange = { startingLocation = it },
                    )
                }
                Row() {
                    Text("How will we get there?", Modifier.padding(10.dp))
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

                    }) {
                    Text("Add Stop")
                }
                Button(
                    onClick = {

                    }) {
                    Text("Return Home")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanTripScreenPreview() {
    TravelTrakerappTheme() {
        PlanTripScreen()
    }
}
