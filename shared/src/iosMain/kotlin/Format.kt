package net.ddns.wsbstonks.shared

import platform.Foundation.*

actual fun relativeTime(fromTimestamp: Long): String {
	return with(NSRelativeDateTimeFormatter()) {
		locale = NSLocale.localeWithLocaleIdentifier(NSLocale.preferredLanguages.first() as String)
		localizedStringForDate(
			NSDate.dateWithTimeIntervalSince1970((fromTimestamp / 1000).toDouble()),
			relativeToDate = NSDate()
		)
	}
}

actual fun localNumber(number: Number, places: Int): String {
	return with(NSNumberFormatter()) {
		locale = NSLocale.localeWithLocaleIdentifier(NSLocale.preferredLanguages.first() as String)
		numberStyle = NSNumberFormatterDecimalStyle
		minimumFractionDigits = places.toULong()
		maximumFractionDigits = places.toULong()
		stringFromNumber(NSNumber(number.toDouble()))!!
	}
}
