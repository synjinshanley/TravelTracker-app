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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.gvsu.cis.traveltracker_app.ui.theme.TravelTrakerappTheme
import kotlinx.coroutines.tasks.await

// style variables
private val NavyBlue  = Color(2, 38, 88)
private val DarkCard  = Color(0xFF2C2C2E)
private val Divider   = Color(0xFF444446)
private val CardShape = RoundedCornerShape(12.dp)


// Local data holder
private data class TripDetail(
    val title: String,
    val startingLocation: String,
    val destination: String,
    val transportation: String,
    val notes: String,
    val stops: List<TripStop>
)


// Reusable card block
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


// One stop row
@Composable
private fun StopDetailCard(stopNumber: Int, stop: TripStop) {
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
            if (stop.transportation.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NavyBlue)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stop.transportation,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }

        HorizontalDivider(color = Divider, thickness = 1.dp)

        if (stop.location.isNotBlank()) {
            LabelledValue(label = "Location", value = stop.location)
        }
        if (stop.notes.isNotBlank()) {
            LabelledValue(label = "Notes", value = stop.notes)
        }
    }
}


// Label + value pair
@Composable
private fun LabelledValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, color = Color(0xFFB0B0B0), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Text(value, color = Color.White, fontSize = 14.sp)
    }
}


// Main screen
@Composable
fun TripDetailsScreen(
    modifier: Modifier = Modifier,
    tripId: String,
    onBack: () -> Unit,
    onEdit: (String) -> Unit
) {
    var tripDetail by remember { mutableStateOf<TripDetail?>(null) }
    var isLoading  by remember { mutableStateOf(true) }
    var loadError  by remember { mutableStateOf<String?>(null) }

    // Load trip + stops from Firestore when the screen opens
    LaunchedEffect(tripId) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        android.util.Log.d("TripDetails", "Current UID: $uid")
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid == null) {
                loadError = "Not signed in."
                isLoading = false
                return@LaunchedEffect
            }

            val db = FirebaseFirestore.getInstance()
            val tripRef = db.collection("users").document(uid)
                .collection("trips").document(tripId)

            val tripSnap = tripRef.get().await()

            val stopsSnap = tripRef.collection("stops")
                .get()
                .await()

            val stops = stopsSnap.documents
                .sortedBy { it.getLong("order") ?: 0L }
                .map { doc ->
                    TripStop(
                        location = doc.getString("location") ?: "",
                        transportation = doc.getString("transportation") ?: "",
                        notes = doc.getString("notes") ?: ""
                    )
                }

            tripDetail = TripDetail(
                title = tripSnap.getString("title") ?: "Untitled Trip",
                startingLocation = tripSnap.getString("startingLocation") ?: "",
                destination = tripSnap.getString("destination") ?: "",
                transportation = tripSnap.getString("transportation") ?: "",
                notes = tripSnap.getString("notes") ?: "",
                stops = stops
            )
        } catch (e: Exception) {
            loadError = "Failed to load trip: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
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

            Button(
                onClick = { onEdit(tripId) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3C))
            ) { Text("Edit") }
        }

        // Body
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NavyBlue)
                }
            }

            loadError != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(loadError ?: "", color = Color.Red, modifier = Modifier.padding(24.dp))
                }
            }

            tripDetail != null -> {
                val trip = tripDetail!!
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(4.dp))

                    // Page title
                    Text(
                        text = trip.title,
                        color = NavyBlue,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 2.dp)
                    )

                    // Trip overview card
                    DetailCard {
                        Text(
                            "Overview",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        HorizontalDivider(color = Divider, thickness = 1.dp)

                        if (trip.startingLocation.isNotBlank())
                            LabelledValue("Starting Location", trip.startingLocation)
                        if (trip.destination.isNotBlank())
                            LabelledValue("Destination", trip.destination)
                        if (trip.transportation.isNotBlank())
                            LabelledValue("Transportation", trip.transportation)
                        if (trip.notes.isNotBlank())
                            LabelledValue("Notes", trip.notes)
                    }

                    // Stops
                    if (trip.stops.isNotEmpty()) {
                        Text(
                            text = "Stops (${trip.stops.size})",
                            color = NavyBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(
                                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 2.dp
                            )
                        )

                        trip.stops.forEachIndexed { index, stop ->
                            StopDetailCard(stopNumber = index + 1, stop = stop)
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripDetailsScreenPreview() {
    TravelTrakerappTheme {
        TripDetailsScreen(tripId = "abcd", onBack = {}, onEdit = {})
    }
}