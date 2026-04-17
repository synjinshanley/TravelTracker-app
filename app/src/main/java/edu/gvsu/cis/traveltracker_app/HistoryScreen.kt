package edu.gvsu.cis.traveltracker_app

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.gvsu.cis.traveltracker_app.TravelViewModel
import kotlinx.coroutines.tasks.await

val BackgroundDark = Color(0xFF1C1C1E)
val CardDark = Color(0xFF2C2C2E)

data class TripUi(
    val id: String,
    val name: String
)

@Composable
fun HistoryScreen(
    viewModel: TravelViewModel,
    onOpenTripDetails: (String) -> Unit,
    onBack: () -> Unit
) {
    var trips     by remember { mutableStateOf(listOf<TripUi>()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }

    // Observe Firebase auth state changes reactively
    var uid by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid) }

    DisposableEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            uid = firebaseAuth.currentUser?.uid
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    // Re-runs whenever uid changes (sign in, sign out, guest switch)
    LaunchedEffect(uid) {
        trips     = emptyList()
        isLoading = true
        loadError = null

        if (uid == null) {
            isLoading = false
            return@LaunchedEffect
        }

        try {
            val snap = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid!!)
                .collection("trips")
                .get()
                .await()

            trips = snap.documents.map { doc ->
                TripUi(
                    id   = doc.id,
                    name = doc.getString("title") ?: "Untitled Trip"
                )
            }
        } catch (e: Exception) {
            loadError = "Failed to load trips: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(top = 50.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Trip History",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(2, 38, 88))
                }
            }

            loadError != null -> {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(loadError ?: "", color = Color.Red)
                }
            }

            trips.isEmpty() -> {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (uid == null) "Sign in to see your trips."
                        else "No trips yet. Plan one!",
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(trips) { trip ->
                        TripHistoryItem(
                            trip    = trip,
                            onClick = { onOpenTripDetails(trip.id) }
                        )
                    }
                }
            }
        }

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(2, 38, 88),
                contentColor   = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.33f)
                .padding(bottom = 16.dp)
        ) {
            Text("Back")
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
        shape  = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(2, 38, 88)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = Color.Gray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = trip.name, color = Color.White)
        }
    }
}