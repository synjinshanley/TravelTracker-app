package edu.gvsu.cis.traveltracker_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme
import kotlinx.coroutines.launch

// style variables
private val NavyBlue  = Color(2, 38, 88)
private val DarkCard  = Color(0xFF2C2C2E)
private val Divider   = Color(0xFF444446)
private val LabelGrey = Color(0xFFB0B0B0)
private val CardShape = RoundedCornerShape(12.dp)
private val FieldShape = RoundedCornerShape(8.dp)


@Composable
private fun EditableValue(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            color = LabelGrey,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            shape = FieldShape,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (singleLine) Modifier
                    else Modifier.height(90.dp)
                ),
            maxLines = if (singleLine) 1 else 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = NavyBlue,
                unfocusedBorderColor = Color(0xFF888888),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}

@Composable
private fun DetailCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(CardShape)
            .background(DarkCard)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}


// Editable version of one stop card
@Composable
private fun EditableStopDetailCard(
    stopNumber: Int,
    stop: TripStop,
    onStopChange: (TripStop) -> Unit,
    onRemove: () -> Unit
) {
    DetailCard {
        // Stop header
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Stop $stopNumber",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Button(
                onClick = onRemove,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(180, 30, 30)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text("Remove", fontSize = 12.sp)
            }
        }

        HorizontalDivider(color = Divider, thickness = 1.dp)

        EditableValue(
            label = "Location",
            value = stop.location,
            onValueChange = { onStopChange(stop.copy(location = it)) }
        )

        EditableValue(
            label = "Transportation",
            value = stop.transportation,
            onValueChange = { onStopChange(stop.copy(transportation = it)) }
        )

        EditableValue(
            label = "Notes",
            value = stop.notes,
            onValueChange = { onStopChange(stop.copy(notes = it)) },
            singleLine = false
        )
    }
}


// Main screen
@Composable
fun TripEditScreen(
    modifier: Modifier = Modifier,
    tripId: String,
    travelViewModel: TravelViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var startingLocation by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var transportation by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var stops by remember { mutableStateOf(listOf<TripStop>()) }

    var isLoading by remember { mutableStateOf(true) }
    var saveError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Load trip + stops from Firestore when the screen opens
    LaunchedEffect(tripId) {
        val tripData = travelViewModel.getTrip(tripId)
        if (tripData != null) {
            val trip = tripData.first
            title = trip.title
            startingLocation = trip.startingLocation
            destination = trip.destination
            transportation = trip.transportation
            notes = trip.notes
            stops = tripData.second
        } else {
            saveError = "Failed to load trip."
        }
        isLoading = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E5EA)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
            ) { Text("Return") }
        }

        // Body
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NavyBlue)
                }
            }

            else -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Spacer(Modifier.height(4.dp))

                    // Page title
                    Text(
                        text = "Edit Trip",
                        color = NavyBlue,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 2.dp)
                    )

                    // Editable version of the Overview Card
                    DetailCard {
                        Text(
                            "Overview",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        HorizontalDivider(color = Divider, thickness = 1.dp)

                        EditableValue(
                            label = "Trip Title",
                            value = title,
                            onValueChange = { title = it }
                        )
                        EditableValue(
                            label = "Starting Location",
                            value = startingLocation,
                            onValueChange = { startingLocation = it }
                        )
                        EditableValue(
                            label = "Destination",
                            value = destination,
                            onValueChange = { destination = it }
                        )
                        EditableValue(
                            label = "Transportation",
                            value = transportation,
                            onValueChange = { transportation = it }
                        )
                        EditableValue(
                            label = "Notes",
                            value = notes,
                            onValueChange = { notes = it },
                            singleLine = false
                        )
                    }

                    // Stops
                    if (stops.isNotEmpty()) {
                        Text(
                            text = "Stops (${stops.size})",
                            color = NavyBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(
                                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 2.dp
                            )
                        )

                        stops.forEachIndexed { index, stop ->
                            EditableStopDetailCard(
                                stopNumber = index + 1,
                                stop = stop,
                                onStopChange = { updated ->
                                    stops = stops.toMutableList().also { it[index] = updated }
                                },
                                onRemove = {
                                    stops = stops.toMutableList().also { it.removeAt(index) }
                                }
                            )
                        }
                    }

                    if (saveError != null) {
                        Text(
                            text = saveError ?: "",
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Action buttons
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                stops = stops + TripStop()
                                scope.launch {
                                    scrollState.animateScrollTo(scrollState.maxValue)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                        ) { Text("+ Add Stop") }

                        Button(
                            onClick = {
                                if (title.isBlank()) {
                                    saveError = "Please enter a trip title."
                                    return@Button
                                }
                                if (startingLocation.isBlank()) {
                                    saveError = "Please enter a starting location."
                                    return@Button
                                }

                                saveError = null
                                scope.launch {
                                    val success = travelViewModel.updateTrip(
                                        tripId = tripId,
                                        title = title,
                                        startingLocation = startingLocation,
                                        destination = destination,
                                        transportation = transportation,
                                        notes = notes,
                                        stops = stops
                                    )

                                    if (success) {
                                        onSave()
                                    } else {
                                        saveError = "Failed to save changes."
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
                        ) { Text("Save") }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripEditScreenPreview() {
    TravelTrakerappTheme {
        Text("TripEditScreen Preview")
    }
}