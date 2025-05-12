package com.mad.weatherwear.screens.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mad.weatherwear.screens.authentication.AuthViewModel
import com.mad.weatherwear.shared.ui.ScreenLayout
import com.mad.weatherwear.ui.theme.Typography

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val preference by profileViewModel.preference.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()

    // Load preference when user is available
    LaunchedEffect(currentUser) {
        currentUser?.id?.let { userId ->
            profileViewModel.loadPreference(userId)
        }

    }

    ScreenLayout(
        titleText = "Profile",
        temperatureText = "Adjust Your Preferences",
        dynamicContent = {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Cold Sensitivity",
                    style = Typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 36.dp)
                )

                Text(
                    text = when (preference?.coldSensitivity) {
                        1 -> "Low"
                        2 -> "Medium"
                        3 -> "High"
                        else -> "Medium"
                    },
                    style = Typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Slider(
                    value = (preference?.coldSensitivity ?: 2).toFloat(),
                    onValueChange = { newValue ->
                        currentUser?.id?.let { userId ->
                            profileViewModel.updateColdSensitivity(userId, newValue.toInt())
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color(0xFFFFA500),
                        inactiveTrackColor = Color(0xFFFFA500)
                    ),

                    valueRange = 1f..3f,
                    steps = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { authViewModel.signOut() },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text("Sign Out")
                }
            }
        }
    )
}