package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme
import kotlinx.coroutines.launch

@Composable
fun PlanTripScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    travelViewModel: TravelViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var startingLocation by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var transportation by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(2, 38, 88)
                )
            ) {
                Text("Return")
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(0xFF2C2C2E))
        ) {
            Column {
                Row {
                    Text("Trip Title", Modifier.padding(10.dp), color = Color.White)
                }
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth(),
                        value = title,
                        onValueChange = { title = it }
                    )
                }
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(0xFF2C2C2E))
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row {
                    Text("Where are we starting?", Modifier.padding(10.dp), color = Color.White)
                }
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth(),
                        value = startingLocation,
                        onValueChange = { startingLocation = it }
                    )
                }

                //Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text("Where are we going?", Modifier.padding(10.dp), color = Color.White)
                }
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth(),
                        value = destination,
                        onValueChange = { destination = it }
                    )
                }

                //Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text("How will we get there?", Modifier.padding(10.dp), color = Color.White)
                }
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth(),
                        value = transportation,
                        onValueChange = { transportation = it }
                    )
                }

                //Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Text("Notes", Modifier.padding(10.dp), color = Color.White)
                }
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth(),
                        value = notes,
                        onValueChange = { notes = it }
                    )
                }
            }
        }

        Box(Modifier.fillMaxWidth().padding(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)) {
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88)
                    )
                ) {
                    Text("Add Stop")
                }

                Button(
                    onClick = {
                        scope.launch {
                            val tripId = travelViewModel.createTrip(
                                title = title,
                                startingLocation = startingLocation,
                                destination = destination,
                                transportation = transportation,
                                notes = notes
                            )

                            if (tripId != null) {
                                onBack()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(2, 38, 88)
                    )
                ) {
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
        PlanTripScreen(onBack = {})
    }
}