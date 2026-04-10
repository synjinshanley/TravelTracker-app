package edu.gvsu.cis.traveltracker_app

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Trip(
    val title: String = "",
    val startingLocation: String = "",
    val destination: String = "",
    val transportation: String = "",
    val notes: String = ""
)

class TravelViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun createTrip(
        title: String,
        startingLocation: String,
        destination: String,
        transportation: String,
        notes: String
    ): String? {
        return try {
            val uid = auth.currentUser?.uid ?: return null

            val trip = Trip(
                title = title,
                startingLocation = startingLocation,
                destination = destination,
                transportation = transportation,
                notes = notes
            )

            val tripRef = db.collection("users")
                .document(uid)
                .collection("trips")
                .document()

            tripRef.set(trip).await()
            tripRef.id
        } catch (e: Exception) {
            null
        }
    }
}