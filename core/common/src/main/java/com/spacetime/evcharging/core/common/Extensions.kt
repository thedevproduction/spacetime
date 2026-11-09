package com.spacetime.evcharging.core.common

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Long.toFormattedCurrency(currencyCode: String = Constants.CURRENCY_INR): String {
    val majorAmount = this / 100.0
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    try {
        format.currency = Currency.getInstance(currencyCode)
    } catch (_: Exception) {}
    return format.format(majorAmount)
}

fun Long.toKwh(): String {
    val kwh = this / 1000.0
    return "%.2f kWh".format(Locale.ENGLISH, kwh)
}

fun Long.toKw(): String {
    val kw = this / 1000.0
    return "%.1f kW".format(Locale.ENGLISH, kw)
}

fun String.maskPhoneNumber(): String {
    if (length < 10) return this
    val visibleStart = take(3)
    val visibleEnd = takeLast(4)
    return "$visibleStart****$visibleEnd"
}
