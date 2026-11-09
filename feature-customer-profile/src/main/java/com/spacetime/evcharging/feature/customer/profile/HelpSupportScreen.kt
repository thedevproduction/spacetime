package com.spacetime.evcharging.feature.customer.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun HelpSupportScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val categories = listOf(
        "CHARGER_START" to "Charging did not start",
        "CHARGER_STOPPED" to "Charger stopped unexpectedly",
        "PAYMENT_ISSUE" to "Payment authorization issue",
        "INCORRECT_BILLING" to "Incorrect billing / energy mismatch",
        "REFUND_REQUEST" to "Refund request",
        "STATION_COMPLAINT" to "Station complaint"
    )

    var selectedCategory by remember { mutableStateOf(categories.first().first) }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Help & Support Tickets",
                showBack = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("Submit Support Issue", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
            Text("Our EV mobility operations team handles tickets within a 4-hour SLA.", fontSize = 13.sp, color = SecondaryText)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Problem Category", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
            Spacer(modifier = Modifier.height(8.dp))

            categories.forEach { (catKey, catLabel) ->
                val isSelected = selectedCategory == catKey
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Primary.copy(alpha = 0.08f) else CardBackground,
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) Primary else Border),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedCategory = catKey }
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = isSelected, onClick = { selectedCategory = catKey }, colors = RadioButtonDefaults.colors(selectedColor = Primary))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = catLabel, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, color = MainText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedInput(
                value = description,
                onValueChange = { description = it },
                label = "Describe what happened (e.g. error code on screen)...",
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Submit Ticket",
                onClick = {
                    viewModel.submitSupportTicket(selectedCategory, description, null)
                },
                enabled = description.isNotBlank()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Ticket Acknowledgment Dialog
        uiState.submittedTicket?.let { ticket ->
            AlertDialog(
                onDismissRequest = {
                    viewModel.clearSubmittedTicket()
                    onNavigateBack()
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Success)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ticket Created")
                    }
                },
                text = {
                    Column {
                        Text("Your ticket reference number is:", fontSize = 13.sp, color = SecondaryText)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(ticket.ticketNumber, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("An EV technical support engineer has been notified and will review your session logs.", fontSize = 13.sp, color = MainText)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearSubmittedTicket()
                            onNavigateBack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Done")
                    }
                },
                containerColor = CardBackground
            )
        }
    }
}
