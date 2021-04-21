package net.ddns.wsbstonks.shared

import android.icu.text.*
import android.icu.text.RelativeDateTimeFormatter.Direction.*
import android.icu.text.RelativeDateTimeFormatter.RelativeUnit.*
import androidx.annotation.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import java.util.*
import kotlin.math.*

actual fun HttpClient(): HttpClient = HttpClient(Android) { Json {} }

@RequiresApi(24)
actual fun getRelativeTime(fromTimestamp: Long): String {
	return with(RelativeDateTimeFormatter.getInstance(Locale.getDefault())) {
		val seconds = ((Date().time - fromTimestamp) / 1000.0)
		val minutes = seconds / 60.0
		val hours = minutes / 60.0
		val days = hours / 24.0
		val weeks = days / 7.0
		val months = weeks / 4.348214285714285
		val years = months / 12.0

		when {
			years >= 1 -> format(round(years), LAST, YEARS)
			months >= 1 -> format(round(months), LAST, MONTHS)
			weeks >= 1 -> format(round(weeks), LAST, WEEKS)
			days >= 1 -> format(round(days), LAST, DAYS)
			hours >= 1 -> format(round(hours), LAST, HOURS)
			minutes >= 1 -> format(round(minutes), LAST, MINUTES)
			else -> format(round(seconds), LAST, SECONDS)
		}
	}
}
