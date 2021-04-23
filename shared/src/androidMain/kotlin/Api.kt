package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.engine.android.*

actual fun HttpClient(
	block: HttpClientConfig<*>.() -> Unit
): HttpClient = HttpClient(Android, block)
