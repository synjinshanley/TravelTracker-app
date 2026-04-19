package edu.gvsu.cis.traveltracker_app

import android.app.Application
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


data class Trip(
    val title: String = "",
    val startingLocation: String = "",
    val startingLocationLat: Double = 0.0,
    val startingLocationLng: Double = 0.0,
    val destination: String = "",
    val destinationLat: Double = 0.0,
    val destinationLng: Double = 0.0,
    val transportation: String = "",
    val notes: String = ""
)

// Represents a stop added during trip planning
data class TripStop(
    val location: String = "",
    val transportation: String = "",
    val notes: String = "",
    val stopLatLng: LatLng = LatLng(0.0, 0.0),
    val tripIndex: Int = 0
)


// Address → LatLng (Forward Geocoding)
fun getLatLngFromAddress(context: Context, address: String): LatLng? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val results = geocoder.getFromLocationName(address, 1)
    return if (!results.isNullOrEmpty()) {
        LatLng(results[0].latitude, results[0].longitude)
    } else null
}


class TravelViewModel(application: Application) : AndroidViewModel(application) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _trips = MutableStateFlow<List<Pair<String, Trip>>>(emptyList())
    val trips = _trips.asStateFlow()

    private val _tripsData  = MutableStateFlow<List<Trip>>(emptyList())
    val tripsData = _tripsData.asStateFlow()
    private val _allStops = MutableStateFlow<List<TripStop>>(emptyList())
    val allStops = _allStops.asStateFlow()

    init {
        fetchTrips()
    }

    fun loadAllStops() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            val allStops = mutableListOf<TripStop>()

            val tripsSnapshot = db.collection("users")
                .document(uid)
                .collection("trips")
                .get()
                .await()

            tripsSnapshot.documents.forEachIndexed { tripIndex, tripDoc ->
                // Add starting location as a stop
                val startLat = tripDoc.getDouble("startingLocationLat") ?: 0.0
                val startLng = tripDoc.getDouble("startingLocationLng") ?: 0.0
                allStops.add(
                    TripStop(
                        location = tripDoc.getString("startingLocation") ?: "",
                        transportation = tripDoc.getString("transportation") ?: "",
                        notes = "Start",
                        stopLatLng = LatLng(startLat, startLng),
                        tripIndex = tripIndex
                    )
                )

                // Add middle stops
                val stopsSnapshot = db.collection("users")
                    .document(uid)
                    .collection("trips")
                    .document(tripDoc.id)
                    .collection("stops")
                    .orderBy("order")
                    .get()
                    .await()

                stopsSnapshot.documents.mapNotNullTo(allStops) { doc ->
                    TripStop(
                        location = doc.getString("location") ?: "",
                        transportation = doc.getString("transportation") ?: "",
                        notes = doc.getString("notes") ?: "",
                        stopLatLng = LatLng(
                            doc.getDouble("lat") ?: 0.0,
                            doc.getDouble("lng") ?: 0.0
                        ),
                        tripIndex = tripIndex
                    )
                }

                // Add destination as a stop
                val destLat = tripDoc.getDouble("destinationLat") ?: 0.0
                val destLng = tripDoc.getDouble("destinationLng") ?: 0.0
                allStops.add(
                    TripStop(
                        location = tripDoc.getString("destination") ?: "",
                        transportation = tripDoc.getString("transportation") ?: "",
                        notes = "Destination",
                        stopLatLng = LatLng(destLat, destLng),
                        tripIndex = tripIndex
                    )
                )
            }

            _allStops.value = allStops
        }
    }

    fun fetchTrips() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            try {
                val snapshot = db.collection("users")
                    .document(uid)
                    .collection("trips")
                    .get()
                    .await()

                _trips.value = snapshot.documents.mapNotNull { doc ->
                    val trip = doc.toObject(Trip::class.java) ?: return@mapNotNull null
                    Pair(doc.id, trip)
                }
            } catch (e: Exception) {
                null
            }
        }
    }


    suspend fun createTrip(
        title: String,
        startingLocation: String,
        destination: String,
        transportation: String,
        notes: String,
        stops: List<TripStop> = emptyList()
    ): String? {
        return try {
            val uid = auth.currentUser?.uid ?: return null

            val tripData = mapOf(
                "title"                  to title,
                "startingLocation"       to startingLocation,
                "startingLocationLat"    to addLocationByAddress(startingLocation)?.latitude,
                "startingLocationLng"    to addLocationByAddress(startingLocation)?.longitude,
                "destination"            to destination,
                "destinationLat"         to addLocationByAddress(destination)?.latitude,
                "destinationLng"         to addLocationByAddress(destination)?.longitude,
                "transportation"         to transportation,
                "notes"                  to notes
            )

            // Create the parent trip document
            val tripRef = db.collection("users")
                .document(uid)
                .collection("trips")
                .document()

            tripRef.set(tripData).await()

            // Write each stop into a "stops" subcollection, ordered by index
            stops.forEachIndexed { index, stop ->
                val latlong = addLocationByAddress(stop.location)
                val stopData = mapOf(
                    "location"       to stop.location,
                    "transportation" to stop.transportation,
                    "notes"          to stop.notes,
                    "order"          to index,
                    "lat"            to latlong?.latitude,
                    "lng"            to latlong?.longitude
                )
                tripRef.collection("stops")
                    .document("stop_$index")
                    .set(stopData)
                    .await()
            }

            tripRef.id
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addLocationByAddress(address: String): LatLng? {
        return getLatLngFromAddress(getApplication(), address)
    }
}