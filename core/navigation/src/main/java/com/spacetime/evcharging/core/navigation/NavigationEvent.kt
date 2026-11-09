package com.spacetime.evcharging.core.navigation

sealed interface NavigationEvent {
    data class Navigate(val route: Any) : NavigationEvent
    data object NavigateBack : NavigationEvent
    data class NavigateAndClearStack(val route: Any) : NavigationEvent
}
