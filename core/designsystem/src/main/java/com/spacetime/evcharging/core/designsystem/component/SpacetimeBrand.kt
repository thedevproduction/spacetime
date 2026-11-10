package com.spacetime.evcharging.core.designsystem.component

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.spacetime.evcharging.core.designsystem.R
import com.spacetime.evcharging.core.designsystem.theme.PrimaryLight

@Composable
fun SpacetimeBrandLogo(
    size: Dp = 64.dp,
    cornerRadius: Dp = 20.dp,
    showGlowBorder: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(cornerRadius)
    val baseModifier = modifier
        .size(size)
        .clip(shape)
        .let {
            if (showGlowBorder) {
                it.border(1.5.dp, PrimaryLight.copy(alpha = 0.45f), shape)
            } else {
                it
            }
        }

    Image(
        painter = painterResource(id = R.drawable.spacetime_brand_logo),
        contentDescription = "Spacetime Energy Futures Logo",
        contentScale = ContentScale.Crop,
        modifier = baseModifier
    )
}

@Composable
fun SystemBarAppearance(isDarkBackground: Boolean) {
    val view = LocalView.current
    val systemDark = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        DisposableEffect(view, isDarkBackground, systemDark) {
            val window = (view.context as? Activity)?.window
            val controller = window?.let { WindowCompat.getInsetsController(it, view) }
            if (controller != null) {
                controller.isAppearanceLightStatusBars = !isDarkBackground
            }
            onDispose {
                if (controller != null) {
                    controller.isAppearanceLightStatusBars = !systemDark
                }
            }
        }
    }
}

