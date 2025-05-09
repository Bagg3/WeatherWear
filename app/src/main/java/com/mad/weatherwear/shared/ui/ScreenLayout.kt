package com.mad.weatherwear.shared.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mad.weatherwear.R
import com.mad.weatherwear.ui.theme.Accent
import com.mad.weatherwear.ui.theme.BgPrimary
import com.mad.weatherwear.ui.theme.BgSecondary
import com.mad.weatherwear.ui.theme.BgTertiary
import com.mad.weatherwear.ui.theme.TextPrimary
import com.mad.weatherwear.ui.theme.Typography

@Composable
fun ScreenLayout(
    modifier: Modifier = Modifier,
    titleText: String,
    temperatureText: String,
    weatherConditionText: String,
    dynamicContent: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgTertiary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(BgPrimary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .align(Alignment.BottomCenter)
                    .background(color = BgSecondary)
            )

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
                    .background(color = Accent, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize(0.95f)
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = titleText,
                    style = Typography.titleLarge,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
                Spacer(modifier = Modifier.height(160.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = temperatureText,
                        style = Typography.titleLarge,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = weatherConditionText,
                        style = Typography.labelLarge,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = dynamicContent
        )
    }
}