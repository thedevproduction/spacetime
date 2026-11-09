package com.spacetime.evcharging.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Spacetime shape system.
 *
 * Defines corner radii for various component sizes:
 * - Small: 8dp (chips, small cards)
 * - Medium: 12dp (cards, dialogs)
 * - Large: 16dp (bottom sheets, large cards)
 * - ExtraLarge: 24dp (modals, featured cards)
 */

/** Small corner radius: 8dp */
val SmallShape = RoundedCornerShape(8.dp)

/** Medium corner radius: 12dp */
val MediumShape = RoundedCornerShape(12.dp)

/** Large corner radius: 16dp */
val LargeShape = RoundedCornerShape(16.dp)

/** Extra large corner radius: 24dp */
val ExtraLargeShape = RoundedCornerShape(24.dp)

/**
 * Material 3 Shapes instance configured for Spacetime.
 */
val SpacetimeShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = SmallShape,
    medium = MediumShape,
    large = LargeShape,
    extraLarge = ExtraLargeShape,
)
