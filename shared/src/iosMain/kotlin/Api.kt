package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.json.*
import platform.Foundation.*

actual fun HttpClient(): HttpClient = HttpClient(Ios) { Json {} }

actual fun getRelativeTime(fromTimestamp: Long): String {
	return with(NSRelativeDateTimeFormatter()) {
		locale = NSLocale.localeWithLocaleIdentifier(NSLocale.preferredLanguages.first() as String)
		localizedStringForDate(
			NSDate.dateWithTimeIntervalSince1970((fromTimestamp / 1000).toDouble()),
			relativeToDate = NSDate()
		)
	}
}
