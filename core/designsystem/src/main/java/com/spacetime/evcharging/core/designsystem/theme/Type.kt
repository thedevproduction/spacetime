package com.spacetime.evcharging.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Spacetime Energy Futures typography system.
 *
 * Defines all text styles used throughout the app following the design spec:
 * - Main page title: 24sp, SemiBold
 * - Section title: 18sp, SemiBold
 * - Card title: 16sp, Medium
 * - Body text: 14sp, Normal
 * - Supporting text: 12sp, Normal
 * - Dashboard KPI: 28sp, Bold
 * - Button label: 14sp, Medium
 */

/** Main page title style - 24sp SemiBold */
val PageTitleStyle = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 32.sp,
    letterSpacing = 0.sp,
)

/** Section title style - 18sp SemiBold */
val SectionTitleStyle = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

/** Card title style - 16sp Medium */
val CardTitleStyle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 22.sp,
    letterSpacing = 0.sp,
)

/** Body text style - 14sp Normal */
val BodyTextStyle = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp,
)

/** Supporting text style - 12sp Normal */
val SupportingTextStyle = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp,
)

/** Dashboard KPI value style - 28sp Bold */
val KpiValueStyle = TextStyle(
    fontSize = 28.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 36.sp,
    letterSpacing = 0.sp,
)

/** Button label style - 14sp Medium */
val ButtonLabelStyle = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
)

/**
 * Material 3 Typography instance configured for Spacetime.
 *
 * Mapping:
 * - headlineLarge → Page title (24sp SemiBold)
 * - headlineMedium → Dashboard KPI (28sp Bold)
 * - titleLarge → Section title (18sp SemiBold)
 * - titleMedium → Card title (16sp Medium)
 * - titleSmall → Card subtitle (14sp Medium)
 * - bodyLarge → Body text emphasized (16sp Normal)
 * - bodyMedium → Body text (14sp Normal)
 * - bodySmall → Supporting text (12sp Normal)
 * - labelLarge → Button label (14sp Medium)
 * - labelMedium → Chip/tag text (12sp Medium)
 * - labelSmall → Caption text (11sp Normal)
 */
val SpacetimeTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 44.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
