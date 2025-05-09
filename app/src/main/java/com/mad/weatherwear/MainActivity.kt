package com.mad.weatherwear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mad.weatherwear.authentication.AuthViewModel
import com.mad.weatherwear.authentication.SignInScreen
import com.mad.weatherwear.authentication.SignUpScreen
import com.mad.weatherwear.service.location.LocationService
import com.mad.weatherwear.ui.theme.WeatherWearTheme

class MainActivity : ComponentActivity() {
    private lateinit var mapsClient: FusedLocationProviderClient
    private lateinit var locationService: LocationService
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapsClient = LocationServices.getFusedLocationProviderClient(this)
        locationService = LocationService(this, mapsClient)
        locationService.requestPermission()


        enableEdgeToEdge()
        setContent {
            WeatherWearTheme {
                val navController = rememberNavController()
                val currentUser by authViewModel.currentUser.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        // Start destination depends on whether a user is logged in
                        startDestination = if (currentUser == null) "signin" else "greeting",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("signin") {
                            SignInScreen(
                                authViewModel = authViewModel,
                                onNavigateToSignUp = { navController.navigate("signup") },
                                onSignInSuccess = {
                                    navController.navigate("greeting") {
                                        popUpTo("signin") { inclusive = true } // Clear back stack
                                    }
                                }
                            )
                        }
                        composable("signup") {
                            SignUpScreen(
                                authViewModel = authViewModel,
                                onNavigateToSignIn = { navController.navigate("signin") },
                                onSignUpSuccess = {
                                    navController.navigate("greeting") {
                                        popUpTo("signin") { inclusive = true } // Clear back stack
                                    }
                                }
                            )
                        }
                        composable("greeting") {
                            Greeting(name = "Android")
                        }
                        // Add other destinations here
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherWearTheme {
        Greeting("Android")
    }
}