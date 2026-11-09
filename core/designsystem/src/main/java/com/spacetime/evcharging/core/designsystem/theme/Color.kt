package com.spacetime.evcharging.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// Primary palette
val Primary = Color(0xFF087F5B)
val PrimaryDark = Color(0xFF056044)
val PrimaryLight = Color(0xFF0CA678)
val PrimaryContainer = Color(0xFFB2F2E1)
val OnPrimary = Color.White
val OnPrimaryContainer = Color(0xFF002117)

// Secondary palette
val Secondary = Color(0xFF102A43)
val SecondaryLight = Color(0xFF243B53)
val SecondaryContainer = Color(0xFFD3E3F7)
val OnSecondary = Color.White
val OnSecondaryContainer = Color(0xFF0A1929)

// Background & Surface
val AppBackground = Color(0xFFF6F8FA)
val CardBackground = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFEDF2F7)

// Text
val MainText = Color(0xFF172B4D)
val SecondaryText = Color(0xFF6B778C)
val TertiaryText = Color(0xFF97A0AF)

// Status
val Success = Color(0xFF16A34A)
val SuccessLight = Color(0xFFDCFCE7)
val Warning = Color(0xFFF59E0B)
val WarningLight = Color(0xFFFEF3C7)
val Error = Color(0xFFDC2626)
val ErrorLight = Color(0xFFFEE2E2)

// Borders & Dividers
val Border = Color(0xFFE5E7EB)
val Divider = Color(0xFFE5E7EB)
val DividerColor = Color(0xFFE5E7EB)

// Status Additional
val StatusReserved = Color(0xFF7C3AED)
val StatusReservedBackground = Color(0xFFEDE9FE)
val ErrorBackground = Color(0xFFFEE2E2)

// Dark Theme Colors
val DarkBackground = Color(0xFF121212)
val DarkSurface = Color(0xFF1E1E1E)
val DarkMainText = Color(0xFFE0E0E0)
val DarkSecondaryText = Color(0xFFA0A0A0)
val DarkBorder = Color(0xFF333333)

// Custom Colors class
class SpacetimeColors(
    val success: Color,
    val successLight: Color,
    val warning: Color,
    val warningLight: Color,
    val statusReserved: Color,
    val statusReservedBackground: Color,
)

val LightSpacetimeColors = SpacetimeColors(
    success = Success,
    successLight = SuccessLight,
    warning = Warning,
    warningLight = WarningLight,
    statusReserved = StatusReserved,
    statusReservedBackground = StatusReservedBackground
)

val DarkSpacetimeColors = SpacetimeColors(
    success = Success,
    successLight = Color(0xFF14532D),
    warning = Warning,
    warningLight = Color(0xFF78350F),
    statusReserved = Color(0xFFA78BFA),
    statusReservedBackground = Color(0xFF4C1D95)
)

// Misc
val Shimmer = Color(0xFFE5E7EB)
val Scrim = Color(0x80000000)
