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



data class tripContainer(
    val id: Int,
    val name: String,
    var points: List<savedLocation>
)

data class Trip(
    val title: String = "",
    val startingLocation: String = "",
    val destination: String = "",
    val transportation: String = "",
    val notes: String = ""
)

data class savedLocation(
    val name: String,
    val address: String = "",
    val lat: Double,
    val lng: Double
)

// Represents a stop added during trip planning
data class TripStop(
    val location: String = "",
    val transportation: String = "",
    val notes: String = ""
)


// Address → LatLng (Forward Geocoding)
fun getLatLngFromAddress(context: Context, address: String): LatLng? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val results = geocoder.getFromLocationName(address, 1)
    return if (!results.isNullOrEmpty()) {
        LatLng(results[0].latitude, results[0].longitude)
    } else null
}

// LatLng → Address (Reverse Geocoding)
fun getAddressFromLatLng(context: Context, lat: Double, lng: Double): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val results = geocoder.getFromLocation(lat, lng, 1)
    return if (!results.isNullOrEmpty()) {
        results[0].getAddressLine(0)
    } else null
}


class TravelViewModel(application: Application) : AndroidViewModel(application) {

    private val _trip = MutableStateFlow(tripContainer(1, "My Trip", emptyList()))
    val currentTrip = _trip.asStateFlow()

    private val _locations = MutableStateFlow(listOf<savedLocation>())
    val locations = _locations.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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

            val trip = Trip(
                title = title,
                startingLocation = startingLocation,
                destination = destination,
                transportation = transportation,
                notes = notes
            )

            // Create the parent trip document
            val tripRef = db.collection("users")
                .document(uid)
                .collection("trips")
                .document()

            tripRef.set(trip).await()

            // Write each stop into a "stops" subcollection, ordered by index
            stops.forEachIndexed { index, stop ->
                val stopData = mapOf(
                    "location"       to stop.location,
                    "transportation" to stop.transportation,
                    "notes"          to stop.notes,
                    "order"          to index
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

    fun addLocation(location: savedLocation) {
        _trip.update { currentTrip ->
            currentTrip.copy(points = currentTrip.points + location)
        }
        updateLocations()
    }

    fun addLocationByAddress(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val latLng = getLatLngFromAddress(getApplication(), address)
            latLng?.let {
                addLocation(
                    savedLocation(
                        name = address,
                        address = address,
                        lat = it.latitude,
                        lng = it.longitude
                    )
                )
            }
        }
    }

    fun addLocationByCoords(name: String, lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val address = getAddressFromLatLng(getApplication(), lat, lng)
            addLocation(
                savedLocation(
                    name = name,
                    address = address ?: "Unknown address",
                    lat = lat,
                    lng = lng
                )
            )
        }
    }

    fun updateLocations() {
        _locations.update {
            currentTrip.value.points
        }
    }

    fun updateTrip(newTrip: tripContainer) {
        _trip.update { newTrip }
        updateLocations()
    }
}