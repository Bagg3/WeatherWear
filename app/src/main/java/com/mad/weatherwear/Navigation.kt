package com.mad.weatherwear

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.mad.weatherwear.ui.theme.Accent

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Filled.Home)
    data object Profile : Screen("profile", "Profile", Icons.Filled.Settings)
    data object Weather : Screen("weather", "Weather", Icons.Filled.LocationOn)
    data object Outfit : Screen("outfit", "Outfit", Icons.Filled.Person)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Weather,
    Screen.Outfit,
    Screen.Profile
)

@Composable
fun WeatherIcon() {
    val weatherImageVector = ImageVector.vectorResource(R.drawable.ic_action_name)
    Icon(
        imageVector = weatherImageVector,
        contentDescription = null,
        modifier = Modifier
            .padding(top = 4.dp, end = 4.dp)
            .size(24.dp)
            
    )
}


@Composable
fun AppBottomNavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { screen ->
            val icon: @Composable () -> Unit = {
                when (screen) {
                    Screen.Weather -> WeatherIcon()
                    else -> Icon(screen.icon, contentDescription = screen.title)
                }
            }

            NavigationBarItem(
                icon = icon,
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}