package net.ddns.wsbstonks.shared

expect fun relativeTime(fromTimestamp: Long): String

expect fun localNumber(number: Number, places: Int = 0): String
