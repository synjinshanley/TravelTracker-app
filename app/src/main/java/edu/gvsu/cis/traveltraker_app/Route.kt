package edu.gvsu.cis.traveltraker_app

import kotlinx.serialization.Serializable

sealed class Route {

    @Serializable
    data object Main

    @Serializable
    data object History

    @Serializable
    data class TripDetails(
        val tripID: String
    )

    @Serializable
    data object PlanTrip

    @Serializable
    data object Profile

    @Serializable
    data object CreateLogin

    @Serializable
    data object Login{
    }
}