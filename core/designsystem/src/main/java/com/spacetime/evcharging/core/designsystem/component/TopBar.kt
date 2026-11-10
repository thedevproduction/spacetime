package com.spacetime.evcharging.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacetime.evcharging.core.designsystem.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacetimeTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBrandBadge: Boolean = false,
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationClick: (() -> Unit)? = null,
    notificationCount: Int = 0,
    onProfileClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.background(CardBackground)) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (showBrandBadge) {
                        SpacetimeBrandLogo(
                            size = 34.dp,
                            cornerRadius = 10.dp
                        )
                    }
                    Column {
                        Text(
                            text = title,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                fontSize = 11.sp,
                                color = SecondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
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
                    Box(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(AppBackground)
                            .clickable(onClick = onNotificationClick),
                        contentAlignment = Alignment.Center
                    ) {
                        BadgedBox(
                            badge = {
                                if (notificationCount > 0) {
                                    Badge(
                                        containerColor = Error,
                                        contentColor = Color.White
                                    ) {
                                        Text("$notificationCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MainText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                if (onProfileClick != null) {
                    Surface(
                        shape = CircleShape,
                        color = Primary.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Primary.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .padding(end = 14.dp, start = 4.dp)
                            .size(38.dp)
                            .clickable(onClick = onProfileClick)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CardBackground
            )
        )
        HorizontalDivider(color = Border.copy(alpha = 0.7f), thickness = 0.75.dp)
    }
}
