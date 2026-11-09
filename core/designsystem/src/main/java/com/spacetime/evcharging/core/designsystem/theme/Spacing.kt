package com.spacetime.evcharging.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacetime spacing system.
 *
 * Provides consistent spacing values throughout the app:
 * - xxs = 4dp  (tight padding, icon gaps)
 * - xs  = 8dp  (compact spacing)
 * - sm  = 12dp (standard inner padding)
 * - md  = 16dp (default padding, card content padding)
 * - lg  = 20dp (section gaps)
 * - xl  = 24dp (screen padding, large gaps)
 * - xxl = 32dp (section separators, large margins)
 */
@Immutable
data class Spacing(
    /** 4dp - Tight padding, icon gaps */
    val xxs: Dp = 4.dp,

    /** 8dp - Compact spacing */
    val xs: Dp = 8.dp,

    /** 12dp - Standard inner padding */
    val sm: Dp = 12.dp,

    /** 16dp - Default padding, card content padding */
    val md: Dp = 16.dp,

    /** 20dp - Section gaps */
    val lg: Dp = 20.dp,

    /** 24dp - Screen padding, large gaps */
    val xl: Dp = 24.dp,

    /** 32dp - Section separators, large margins */
    val xxl: Dp = 32.dp,
)

/**
 * CompositionLocal for providing [Spacing] through the composition tree.
 *
 * Access via `LocalSpacing.current` within any composable inside [SpacetimeTheme].
 */
val LocalSpacing = staticCompositionLocalOf { Spacing() }
