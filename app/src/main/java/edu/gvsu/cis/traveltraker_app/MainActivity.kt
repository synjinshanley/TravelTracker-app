package edu.gvsu.cis.traveltraker_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                val nc = rememberNavController()

                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = nc,
                    startDestination = Route.Main
                ) {

                    composable<Route.Main> {
                        MainScreen(
                            onOpenProfile = {
                                nc.navigate(Route.Profile)
                            },
                            onOpenPlanTrip = {
                                nc.navigate(Route.PlanTrip)
                            },
                            onOpenHistory = {
                                nc.navigate(Route.History)
                            }
                        )
                    }

                    composable<Route.History> {

                        val fakeTrips = listOf(
                            TripUi("1", "Chicago Trip"),
                            TripUi("2", "Spring Break Florida"),
                            TripUi("3", "Traverse City Weekend")
                        )

                        HistoryScreen(
                            trips = fakeTrips,
                            onOpenTripDetails = { tripId ->
                                val dest = Route.TripDetails(tripId)
                                nc.navigate(dest)
                            },
                            onBack = {
                                nc.popBackStack()
                            }
                        )
                    }

                    composable<Route.TripDetails> {

                        val temp = it.toRoute<Route.TripDetails>()

                        TripDetailsScreen(
                            tripId = temp.tripID,
                            onBack = {
                                nc.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
