package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.engine.ios.*

actual fun HttpClient(
	block: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(Ios, block)
