package com.spacetime.evcharging.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainText,
                    modifier = Modifier.weight(1f)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Warning,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(rating),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address,
                fontSize = 13.sp,
                color = SecondaryText,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Border)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$availableConnectors/$totalConnectors Available",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (availableConnectors > 0) Success else Error
                    )
                    Text(
                        text = " • $distance",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }
                Text(
                    text = priceRange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
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
        shape = RoundedCornerShape(16.dp),
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
                    fontSize = 13.sp,
                    color = SecondaryText,
                    fontWeight = FontWeight.Medium
                )
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
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
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Secondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Wallet Balance",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = balance,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Button(
                onClick = onAddMoney,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                )
            ) {
                Text("+ Add Money", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
        }
    }
}


