package com.example.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {
    private val isoDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val arabicWeekdayFormatter =
        DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag("ar"))

    fun todayStr(): String = LocalDate.now().format(isoDateFormatter)

    fun nowIso(): String = Instant.now().toString()

    fun parseDate(dateStr: String): LocalDate? =
        runCatching { LocalDate.parse(dateStr, isoDateFormatter) }.getOrNull()

    fun addDays(dateStr: String, days: Int): String {
        val d = parseDate(dateStr) ?: LocalDate.now()
        return d.plusDays(days.toLong()).format(isoDateFormatter)
    }

    fun daysBetween(a: String, b: String): Int {
        val da = parseDate(a) ?: return 0
        val db = parseDate(b) ?: return 0
        return ChronoUnit.DAYS.between(da, db).toInt()
    }

    fun lastNDays(n: Int): List<String> {
        val today = LocalDate.now()
        return (0 until n).map { i ->
            today.minusDays((n - 1 - i).toLong()).format(isoDateFormatter)
        }
    }

    fun dayLabel(dateStr: String): String {
        val d = parseDate(dateStr) ?: return dateStr
        return d.format(arabicWeekdayFormatter)
    }

    fun fmtTime(seconds: Int): String {
        val safe = seconds.coerceAtLeast(0)
        val m = safe / 60
        val s = safe % 60
        return "%02d:%02d".format(Locale.US, m, s)
    }

    fun fmtMinutes(min: Int): String {
        if (min < 60) return "$min د"
        val h = min / 60
        val m = min % 60
        return if (m > 0) "$h س $m د" else "$h س"
    }

    fun fmtDateArabic(isoTimestamp: String): String {
        return runCatching {
            val instant = Instant.parse(isoTimestamp)
            val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.forLanguageTag("ar")))
        }.getOrElse { isoTimestamp.take(10) }
    }

    fun epochDayIndex(): Long = ChronoUnit.DAYS.between(LocalDate.ofEpochDay(0), LocalDate.now())

    fun isValidDay(s: String): Boolean = Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(s) && parseDate(s) != null

    fun isValidTime(s: String): Boolean = Regex("^\\d{2}:\\d{2}$").matches(s)
}
