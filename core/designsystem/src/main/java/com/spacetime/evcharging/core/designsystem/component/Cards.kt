package com.spacetime.evcharging.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun StationCard(
    name: String,
    address: String,
    distance: String,
    availableConnectors: Int,
    totalConnectors: Int,
    priceRange: String,
    rating: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAvailable = availableConnectors > 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "%.1f".format(rating),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = TertiaryText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = address,
                    fontSize = 13.sp,
                    color = SecondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isAvailable) SuccessLight else ErrorLight
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isAvailable) Success else Error)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "$availableConnectors/$totalConnectors Available",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isAvailable) Success else Error
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AppBackground
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = null,
                            tint = SecondaryText,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = distance,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = SecondaryText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Border.copy(alpha = 0.7f), thickness = 0.75.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = priceRange,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Primary
                    )
                    Text(
                        text = "  starting tariff",
                        fontSize = 11.sp,
                        color = SecondaryText
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "View Hub",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = Primary
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = SecondaryText,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (icon != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = iconTint.copy(alpha = 0.12f),
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MainText
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = SecondaryText
                )
            }
        }
    }
}

@Composable
fun WalletBalanceCard(
    balance: String,
    onAddMoney: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0A1E33),
                        Color(0xFF102A43),
                        Color(0xFF074D3C)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.height * 0.85f,
                center = Offset(size.width * 0.92f, size.height * 0.15f)
            )
            drawCircle(
                color = Color(0xFF0CA678).copy(alpha = 0.14f),
                radius = size.height * 0.55f,
                center = Offset(size.width * 0.85f, size.height * 0.9f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color(0xFF6EE7B7),
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "SPACETIME WALLET",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = Color.White.copy(alpha = 0.72f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = balance,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Instant auto-pay enabled",
                        fontSize = 11.sp,
                        color = Color(0xFF6EE7B7).copy(alpha = 0.9f)
                    )
                }

                Button(
                    onClick = onAddMoney,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryLight,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Money", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
