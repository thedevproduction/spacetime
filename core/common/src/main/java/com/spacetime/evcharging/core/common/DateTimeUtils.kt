package com.spacetime.evcharging.core.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtils {
    private val IST_ZONE = ZoneId.of("Asia/Kolkata")
    private val DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
    private val DISPLAY_TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    private val DISPLAY_DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH)

    fun formatDisplayDateTime(isoString: String?): String {
        if (isoString.isNullOrBlank()) return ""
        return try {
            val instant = Instant.parse(isoString)
            instant.atZone(IST_ZONE).format(DISPLAY_DATETIME_FORMAT)
        } catch (e: Exception) {
            isoString
        }
    }

    fun formatDisplayDate(isoString: String?): String {
        if (isoString.isNullOrBlank()) return ""
        return try {
            val instant = Instant.parse(isoString)
            instant.atZone(IST_ZONE).format(DISPLAY_DATE_FORMAT)
        } catch (e: Exception) {
            isoString
        }
    }

    fun formatDisplayTime(isoString: String?): String {
        if (isoString.isNullOrBlank()) return ""
        return try {
            val instant = Instant.parse(isoString)
            instant.atZone(IST_ZONE).format(DISPLAY_TIME_FORMAT)
        } catch (e: Exception) {
            isoString
        }
    }
}
