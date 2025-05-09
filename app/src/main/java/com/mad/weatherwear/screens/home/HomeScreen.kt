package com.mad.weatherwear.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mad.weatherwear.ui.theme.*

@Composable
fun getNavigationBarHeight(): Dp {
    val insets = WindowInsets.systemBars
    val bottomInset = insets.getBottom(LocalDensity.current)
    return with(LocalDensity.current) { bottomInset.toDp() }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val navBarHeight = getNavigationBarHeight()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight - navBarHeight)
            .background(BgPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(BgPrimary)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(BgSecondary)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .background(BgTertiary)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weatherwear",
                style = Typography.titleLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = screenHeight * 0.16f, bottom = 12.dp)
                    .size(150.dp)
                    .background(color = Accent, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "15°C",
                style = Typography.titleLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Sunny",
                style = Typography.labelSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}