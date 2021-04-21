package net.ddns.wsbstonks.shared

import android.icu.text.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*

actual fun HttpClient(): HttpClient = HttpClient(Android) { Json {} }

actual fun getRelativeTime(fromTimestamp: Long): String {
	val formatter = RelativeDateTimeFormatter.getInstance()

}
