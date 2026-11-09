package com.spacetime.evcharging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.spacetime.evcharging.core.designsystem.theme.AppBackground
import com.spacetime.evcharging.core.designsystem.theme.SpacetimeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpacetimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppBackground
                ) {
                    SpacetimeNavHost()
                }
            }
        }
    }
}
