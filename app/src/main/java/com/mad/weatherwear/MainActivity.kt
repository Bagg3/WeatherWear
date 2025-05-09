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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mad.weatherwear.authentication.AuthViewModel
import com.mad.weatherwear.authentication.SignInScreen
import com.mad.weatherwear.authentication.SignUpScreen
import com.mad.weatherwear.shared.location.LocationService
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

                // Determine the start destination based on login state
                val startDestination = if (currentUser == null) "signin" else Screen.Home.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Show BottomNavigationBar only if the user is logged in and not on auth screens
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        if (currentUser != null && currentRoute != "signin" && currentRoute != "signup") {
                            AppBottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("signin") {
                            SignInScreen(
                                authViewModel = authViewModel,
                                onNavigateToSignUp = { navController.navigate("signup") },
                                onSignInSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo("signin") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("signup") {
                            SignUpScreen(
                                authViewModel = authViewModel,
                                onNavigateToSignIn = { navController.navigate("signin") },
                                onSignUpSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo("signin") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Screen.Home.route) {
                            HomeScreen() // Replace with actual user name or data
                        }
                        composable(Screen.Profile.route) {
                            // Replace with your ProfileScreen composable
                            ProfileScreen()
                        }

                        composable(Screen.Weather.route) {
                            // Replace with your WeatherScreen composable
                            WeatherScreen()
                        }

                        composable(Screen.Outfit.route) {
                            // Replace with your OutfitScreen composable
                            OutfitScreen()
                        }
                        // Add other destinations here
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Hello",
        modifier = modifier
    )
}

// Placeholder for ProfileScreen - replace with your actual screen
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Profile Screen",
        modifier = modifier
    )
}

// Placeholder for WeatherScreen - replace with your actual screen
@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Weather Screen",
        modifier = modifier
    )
}

// Placeholder for OutfitScreen - replace with your actual screen
@Composable
fun OutfitScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Outfit Screen",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherWearTheme {
        HomeScreen( )
    }
}