package com.mad.weatherwear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mad.weatherwear.screens.authentication.AuthViewModel
import com.mad.weatherwear.screens.authentication.Email
import com.mad.weatherwear.screens.authentication.Password
import com.mad.weatherwear.screens.home.HomeScreen
import com.mad.weatherwear.screens.outfit.OutfitScreen
import com.mad.weatherwear.screens.profile.ProfileScreen
import com.mad.weatherwear.screens.profile.ProfileViewModel
import com.mad.weatherwear.screens.weather.WeatherScreen
import com.mad.weatherwear.screens.weather.WeatherViewModel
import com.mad.weatherwear.shared.ui.AuthScreenLayout
import com.mad.weatherwear.ui.theme.WeatherWearTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            val currentUser by authViewModel.currentUser.collectAsState()
            val authError by authViewModel.authError.collectAsState()
            WeatherWearTheme {
                val navController = rememberNavController()
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
                            var emailInput by remember { mutableStateOf("") }
                            var passwordInput by remember { mutableStateOf("") }
                            var formErrorMessage by remember { mutableStateOf<String?>(null) }

                            AuthScreenLayout(
                                titleText = "Sign In",
                                subTitleText = "Welcome back!",
                                emailInput = emailInput,
                                onEmailInputChange = { emailInput = it },
                                passwordInput = passwordInput,
                                onPasswordInputChange = { passwordInput = it },
                                buttonText = "Sign In",
                                formErrorMessage = formErrorMessage,
                                onButtonClick = {
                                    formErrorMessage = null
                                    if (!Email.validate(emailInput)) {
                                        formErrorMessage = "Invalid email format"
                                    } else if (!Password.validate(passwordInput)) {
                                        formErrorMessage =
                                            "Password must be at least 8 characters, including letters and numbers."
                                    } else {
                                        authViewModel.signIn(
                                            Email(emailInput),
                                            Password(passwordInput)
                                        )
                                    }
                                },
                                navigationText = "Don't have an account? Sign Up",
                                onNavigationClick = { navController.navigate("signup") },
                                authError = authError,
                                clearError = { authViewModel.clearError() } // Clear the error after displaying
                            )
                        }
                        composable("signup") {
                            var emailInput by remember { mutableStateOf("") }
                            var passwordInput by remember { mutableStateOf("") }
                            var formErrorMessage by remember { mutableStateOf<String?>(null) }

                            AuthScreenLayout(
                                titleText = "Sign Up",
                                subTitleText = "Create your account!",
                                emailInput = emailInput,
                                onEmailInputChange = { emailInput = it },
                                passwordInput = passwordInput,
                                onPasswordInputChange = { passwordInput = it },
                                buttonText = "Sign Up",
                                formErrorMessage = formErrorMessage,
                                onButtonClick = {
                                    formErrorMessage = null
                                    if (!Email.validate(emailInput)) {
                                        formErrorMessage = "Invalid email format"
                                    } else if (!Password.validate(passwordInput)) {
                                        formErrorMessage =
                                            "Password must be at least 8 characters, including letters and numbers."
                                    } else {
                                        authViewModel.signUp(
                                            Email(emailInput),
                                            Password(passwordInput)
                                        )
                                    }
                                },
                                navigationText = "Already have an account? Sign In",
                                onNavigationClick = { navController.navigate("signin") },
                                authError = authError,
                                clearError = { authViewModel.clearError() } // Clear the error after displaying
                            )
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(weatherViewModel = weatherViewModel)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                authViewModel = authViewModel,
                            )
                        }

                        composable(Screen.Weather.route) {
                            WeatherScreen(viewModel = weatherViewModel)
                        }

                        composable(Screen.Outfit.route) {
                            OutfitScreen(
                                weatherViewModel = weatherViewModel,
                                profileViewModel = profileViewModel, userId = currentUser?.id ?: ""
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherWearTheme {
        HomeScreen(weatherViewModel = viewModel<WeatherViewModel>())
    }
}