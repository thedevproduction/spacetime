package com.spacetime.evcharging.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Material 3 light color scheme mapped to Spacetime brand colors.
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = PrimaryDark,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD2E4FF),
    onSecondaryContainer = Secondary,
    tertiary = StatusReserved,
    onTertiary = Color.White,
    tertiaryContainer = StatusReservedBackground,
    onTertiaryContainer = Color(0xFF4C1D95),
    error = Error,
    onError = Color.White,
    errorContainer = ErrorBackground,
    onErrorContainer = Color(0xFF7F1D1D),
    background = AppBackground,
    onBackground = MainText,
    surface = CardBackground,
    onSurface = MainText,
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = SecondaryText,
    outline = Border,
    outlineVariant = Divider,
    inverseSurface = Secondary,
    inverseOnSurface = Color.White,
    inversePrimary = Color(0xFF4FD1C5),
    surfaceTint = Primary,
)

/**
 * Material 3 dark color scheme mapped to Spacetime brand colors.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1A3A2E),
    onPrimaryContainer = Color(0xFF4FD1C5),
    secondary = Color(0xFFCBD5E0),
    onSecondary = Color(0xFF1A202C),
    secondaryContainer = Color(0xFF2D3748),
    onSecondaryContainer = Color(0xFFE2E8F0),
    tertiary = Color(0xFFA78BFA),
    onTertiary = Color(0xFF1A1A2E),
    tertiaryContainer = Color(0xFF4C1D95),
    onTertiaryContainer = Color(0xFFEDE9FE),
    error = Color(0xFFEF4444),
    onError = Color.White,
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFECACA),
    background = DarkBackground,
    onBackground = DarkMainText,
    surface = DarkSurface,
    onSurface = DarkMainText,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = DarkSecondaryText,
    outline = DarkBorder,
    outlineVariant = Color(0xFF2A2A2A),
    inverseSurface = Color(0xFFE1E1E1),
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = PrimaryDark,
    surfaceTint = Primary,
)

/**
 * CompositionLocal for providing [SpacetimeColors] through the composition tree.
 */
val LocalSpacetimeColors = staticCompositionLocalOf { LightSpacetimeColors }

/**
 * Main theme composable for the Spacetime Energy Futures app.
 *
 * Sets up:
 * - Material 3 ColorScheme (light/dark)
 * - Custom [SpacetimeColors] via [LocalSpacetimeColors]
 * - [Spacing] via [LocalSpacing]
 * - [SpacetimeTypography]
 * - [SpacetimeShapes]
 * - Status bar color
 *
 * @param darkTheme Whether to use the dark color scheme. Defaults to system setting.
 * @param content The composable content to theme.
 */
@Composable
fun SpacetimeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val spacetimeColors = if (darkTheme) DarkSpacetimeColors else LightSpacetimeColors

    // Configure system bar icon contrast to match the active light/dark theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalSpacetimeColors provides spacetimeColors,
        LocalSpacing provides Spacing(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SpacetimeTypography,
            shapes = SpacetimeShapes,
            content = content,
        )
    }
}

/**
 * Convenience accessor for Spacetime custom colors.
 *
 * Usage: `SpacetimeTheme.colors.primary`
 */
object SpacetimeTheme {

    /**
     * The current [SpacetimeColors] provided by [LocalSpacetimeColors].
     */
    val colors: SpacetimeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacetimeColors.current

    /**
     * The current [Spacing] provided by [LocalSpacing].
     */
    val spacing: Spacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}
