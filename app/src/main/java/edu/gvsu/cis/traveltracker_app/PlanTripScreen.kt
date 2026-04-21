package edu.gvsu.cis.traveltracker_app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme
import kotlinx.coroutines.launch


// Style variables
private val NavyBlue   = Color(2, 38, 88)
private val DarkCard   = Color(0xFF2C2C2E)
private val Divider    = Color(0xFF444446)
private val LabelGrey  = Color(0xFFB0B0B0)
private val CardShape  = RoundedCornerShape(12.dp)
private val FieldShape = RoundedCornerShape(8.dp)


// Single line text field style
@Composable
fun TripField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = LabelGrey, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            shape = FieldShape,
            modifier = Modifier.fillMaxWidth(),
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

// multi line notes field style
@Composable
private fun TripNotesField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, color = LabelGrey, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = FieldShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            maxLines = 3,
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


// reusable card wrapper style for both main card and stop cards
@Composable
private fun SectionCard(
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        content()
    }
}


// Stop card
@Composable
private fun StopCard(
    stopNumber: Int,
    stop: TripStop,
    onStopChange: (TripStop) -> Unit,
    onRemove: () -> Unit
) {
    SectionCard {
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

        TripField(
            label = "Location",
            value = stop.location,
            onValueChange = { onStopChange(stop.copy(location = it)) }
        )
        TripField(
            label = "Transportation to this stop",
            value = stop.transportation,
            onValueChange = { onStopChange(stop.copy(transportation = it)) }
        )
        TripNotesField(
            label = "Notes",
            value = stop.notes,
            onValueChange = { onStopChange(stop.copy(notes = it)) }
        )
    }
}


// Main screen
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
    var stops by remember { mutableStateOf(listOf<TripStop>()) }
    var saveError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E5EA))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top bar
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 12.dp, end = 12.dp)
        ) {
            Button(
                onClick = onBack,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
            ) { Text("Return") }
        }

        // Page title
        Text(
            text = "Plan a Trip",
            color = NavyBlue,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 2.dp)
        )

        // Main trip details card
        SectionCard {
            Text(
                text = "Trip Details",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            HorizontalDivider(color = Divider, thickness = 1.dp)

            TripField(
                label = "Trip Title",
                value = title,
                onValueChange = { title = it }
            )
            TripField(
                label = "Starting Location",
                value = startingLocation,
                onValueChange = { startingLocation = it }
            )
            TripField(
                label = "Destination",
                value = destination,
                onValueChange = { destination = it }
            )
            TripField(
                label = "Transportation",
                value = transportation,
                onValueChange = { transportation = it }
            )
            TripNotesField(
                label = "Notes",
                value = notes,
                onValueChange = { notes = it }
            )
        }

        // Stops section label
        if (stops.isNotEmpty()) {
            Text(
                text = "Stops (${stops.size})",
                color = NavyBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Stop cards
        stops.forEachIndexed { index, stop ->
            AnimatedVisibility(
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                StopCard(
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

        // Error message
        if (saveError != null) {
            Text(
                text = saveError ?: "",
                color = Color(200, 50, 50),
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
                onClick = { stops = stops + TripStop() },
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
                    scope.launch {
                        val startLatLng = travelViewModel.addLocationByAddress(startingLocation)
                        val destLatLng = travelViewModel.addLocationByAddress(destination)

                        if (startLatLng == null) {
                            saveError = "Starting location not found, try to be more specific (City, State, Country)"
                            return@launch
                        }
                        if (destLatLng == null) {
                            saveError = "Destination not found, try to be more specific (City, State, Country)"
                            return@launch
                        }

                        val invalidStop = stops.indexOfFirst { stop ->
                            travelViewModel.addLocationByAddress(stop.location) == null
                        }
                        if (invalidStop != -1) {
                            saveError = "Stop ${invalidStop + 1} location not found, try to be more specific"
                            return@launch
                        }

                        saveError = null
                        val tripId = travelViewModel.createTrip(
                            title = title,
                            startingLocation = startingLocation,
                            destination = destination,
                            transportation = transportation,
                            notes = notes,
                            stops = stops
                        )
                        if (tripId != null) {
                            onBack()
                        } else {
                            saveError = "Failed to save trip. Are you signed in?"
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = NavyBlue)
            ) { Text("Finalize") }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PlanTripScreenPreview() {
    TravelTrakerappTheme {
        PlanTripScreen(onBack = {})
    }
}