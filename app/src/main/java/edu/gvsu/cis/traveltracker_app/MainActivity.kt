package edu.gvsu.cis.traveltracker_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    private val travelViewModel  by viewModels<TravelViewModel>()
    private val loginViewModel   by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Firebase.initialize(this)

        val startDestination: Any =
            if (FirebaseAuth.getInstance().currentUser != null) Route.Main
            else Route.Login

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                val nc = rememberNavController()

                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = nc,
                    startDestination = startDestination
                ) {

                    composable<Route.Main> {
                        MainScreen(
                            viewModel = travelViewModel,
                            onOpenProfile = { nc.navigate(Route.Profile) },
                            onOpenPlanTrip = { nc.navigate(Route.PlanTrip) },
                            onOpenHistory = { nc.navigate(Route.History) }
                        )
                    }

                    composable<Route.PlanTrip> {
                        PlanTripScreen(
                            travelViewModel = travelViewModel,
                            onBack = { nc.popBackStack() }
                        )
                    }

                    composable<Route.History> {

                        HistoryScreen(
                            viewModel = travelViewModel,
                            onOpenTripDetails = { tripId ->
                                nc.navigate(Route.TripDetails(tripId))
                            },
                            onBack = { nc.popBackStack() }
                        )
                    }

                    composable<Route.TripDetails> {
                        val temp = it.toRoute<Route.TripDetails>()
                        TripDetailsScreen(
                            tripId = temp.tripID,
                            travelViewModel = travelViewModel,
                            onBack = { nc.popBackStack() },
                            onEdit = { tripId ->
                                nc.navigate(Route.TripEdit(tripId))
                            },
                            onDelete = {
                                nc.navigate(Route.History) {
                                    popUpTo(Route.TripDetails(temp.tripID)) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable<Route.TripEdit> {
                        val temp = it.toRoute<Route.TripEdit>()
                        TripEditScreen(
                            tripId = temp.tripID,
                            travelViewModel = travelViewModel,
                            onBack = { nc.popBackStack() },
                            onSave = { nc.popBackStack() }
                        )
                    }

                    composable<Route.Profile> {
                        ProfileScreen(
                            loginViewModel = loginViewModel,
                            onChangeProfile = { nc.navigate(Route.Login) },
                            onHome = { nc.popBackStack(route = Route.Main, inclusive = false) },
                            onSignOut  = {
                                // Pop everything back to Login so the user must sign in again
                                nc.navigate(Route.Login) {
                                    popUpTo(Route.Main) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable<Route.CreateLogin> {
                        CreateLoginScreen(
                            loginViewModel = loginViewModel,
                            onCreateLogin = { nc.navigate(Route.Main) },
                            onBack = { nc.popBackStack() }
                        )
                    }


                    composable<Route.Login> {
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onCreateNewAccount = { nc.navigate(Route.CreateLogin) },
                            onGuestLogin = { nc.navigate(Route.Main) },
                            onLogin = { nc.navigate(Route.Main) }
                        )
                    }
                }
            }
        }
    }
}