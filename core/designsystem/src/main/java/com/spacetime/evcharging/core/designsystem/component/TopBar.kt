package com.spacetime.evcharging.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacetime.evcharging.core.designsystem.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacetimeTopBar(
    title: String,
    modifier: Modifier = Modifier,
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationClick: (() -> Unit)? = null,
    notificationCount: Int = 0,
    onProfileClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MainText
                    )
                }
            }
        },
        actions = {
            if (onNotificationClick != null) {
                IconButton(onClick = onNotificationClick) {
                    BadgedBox(
                        badge = {
                            if (notificationCount > 0) {
                                Badge(containerColor = Error) {
                                    Text("$notificationCount")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MainText
                        )
                    }
                }
            }
            if (onProfileClick != null) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Primary.copy(alpha = 0.15f))
                        .clickable(onClick = onProfileClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CardBackground
        ),
        modifier = modifier
    )
}
