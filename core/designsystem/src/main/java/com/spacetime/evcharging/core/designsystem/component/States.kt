package com.spacetime.evcharging.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun LoadingScreen(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Primary, strokeWidth = 3.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, fontSize = 14.sp, color = SecondaryText)
        }
    }
}

@Composable
fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MainText,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            fontSize = 14.sp,
            color = SecondaryText,
            textAlign = TextAlign.Center
        )
        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryButton(
                text = actionText,
                onClick = onAction,
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    retryText: String = "Retry"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Something went wrong",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            fontSize = 14.sp,
            color = SecondaryText,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        PrimaryButton(
            text = retryText,
            onClick = onRetry,
            modifier = Modifier.width(160.dp)
        )
    }
}

@Composable
fun NetworkErrorBanner(
    isOffline: Boolean,
    modifier: Modifier = Modifier
) {
    if (isOffline) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Error)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No internet connection",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
