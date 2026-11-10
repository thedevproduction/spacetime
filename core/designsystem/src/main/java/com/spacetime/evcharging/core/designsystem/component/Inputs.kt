package com.spacetime.evcharging.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun OutlinedInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = singleLine,
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            leadingIcon = leadingIcon?.let {
                { Icon(imageVector = it, contentDescription = null, tint = SecondaryText) }
            },
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
                focusedLabelColor = Primary,
                unfocusedLabelColor = SecondaryText
            )
        )
        if (error != null) {
            Text(
                text = error,
                color = Error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun PhoneNumberInput(
    countryCode: String = "+91",
    phoneNumber: String,
    onPhoneChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Border),
                color = CardBackground,
                modifier = Modifier.height(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 14.dp)
                ) {
                    Text(
                        text = countryCode,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MainText
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 10 && it.all { char -> char.isDigit() }) onPhoneChange(it) },
                label = { Text("Mobile Number") },
                placeholder = { Text("Enter 10-digit number") },
                modifier = Modifier.weight(1f),
                isError = error != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Border,
                    focusedLabelColor = Primary,
                    unfocusedLabelColor = SecondaryText
                )
            )
        }
        if (error != null) {
            Text(
                text = error,
                color = Error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun OtpInput(
    length: Int = 6,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= length && it.all { c -> c.isDigit() }) {
                    onValueChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until length) {
                        val char = value.getOrNull(i)?.toString() ?: ""
                        val isFocused = value.length == i
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .border(
                                    width = if (isFocused) 2.dp else 1.dp,
                                    color = when {
                                        error != null -> Error
                                        isFocused -> Primary
                                        char.isNotEmpty() -> Secondary
                                        else -> Border
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MainText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        )
        if (error != null) {
            Text(
                text = error,
                color = Error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Where do you want to charge?",
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        enabled = enabled,
        placeholder = { Text(placeholder, color = SecondaryText, fontSize = 14.sp) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SecondaryText)
        },
        trailingIcon = if (query.isNotEmpty() && enabled) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear search", tint = SecondaryText)
                }
            }
        } else null,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground,
            disabledContainerColor = CardBackground,
            focusedBorderColor = Primary,
            unfocusedBorderColor = Border,
            disabledBorderColor = Border,
            disabledTextColor = MainText,
            disabledPlaceholderColor = SecondaryText,
            disabledLeadingIconColor = SecondaryText
        )
    )
}
