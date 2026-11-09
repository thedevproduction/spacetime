package com.spacetime.evcharging.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacetime.evcharging.core.common.model.ChargerStatus
import com.spacetime.evcharging.core.common.model.PaymentStatus
import com.spacetime.evcharging.core.common.model.SessionStatus
import com.spacetime.evcharging.core.designsystem.theme.*

enum class ChargerDisplayStatus(
    val label: String,
    val color: Color,
    val backgroundColor: Color
) {
    AVAILABLE("Available", Success, SuccessLight),
    CHARGING("Charging", Color(0xFF2563EB), Color(0xFFDBEAFE)),
    OCCUPIED("Occupied", Warning, WarningLight),
    RESERVED("Reserved", Color(0xFF7C3AED), Color(0xFFEDE9FE)),
    OFFLINE("Offline", Color(0xFF6B7280), Color(0xFFF3F4F6)),
    FAULTED("Fault", Error, ErrorLight),
    MAINTENANCE("Maintenance", Color(0xFFEA580C), Color(0xFFFFEDD5))
}

@Composable
fun ChargerStatusChip(
    status: ChargerStatus,
    modifier: Modifier = Modifier
) {
    val display = when (status) {
        ChargerStatus.AVAILABLE -> ChargerDisplayStatus.AVAILABLE
        ChargerStatus.CHARGING -> ChargerDisplayStatus.CHARGING
        ChargerStatus.PREPARING, ChargerStatus.FINISHING -> ChargerDisplayStatus.OCCUPIED
        ChargerStatus.RESERVED -> ChargerDisplayStatus.RESERVED
        ChargerStatus.FAULTED -> ChargerDisplayStatus.FAULTED
        ChargerStatus.OFFLINE -> ChargerDisplayStatus.OFFLINE
        ChargerStatus.MAINTENANCE, ChargerStatus.UNAVAILABLE -> ChargerDisplayStatus.MAINTENANCE
        else -> ChargerDisplayStatus.OFFLINE
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(display.backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(display.color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = display.label,
            color = display.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SessionStatusChip(
    status: SessionStatus,
    modifier: Modifier = Modifier
) {
    val (color, bgColor) = when (status) {
        SessionStatus.CHARGING -> Color(0xFF2563EB) to Color(0xFFDBEAFE)
        SessionStatus.COMPLETED -> Success to SuccessLight
        SessionStatus.FAILED, SessionStatus.CANCELLED -> Error to ErrorLight
        SessionStatus.STARTING, SessionStatus.STOPPING -> Warning to WarningLight
        else -> Color(0xFF6B7280) to Color(0xFFF3F4F6)
    }

    Text(
        text = status.displayName,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
