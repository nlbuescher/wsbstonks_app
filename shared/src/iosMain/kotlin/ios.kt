package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.engine.ios.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

suspend fun test(): String {
	val client = HttpClient(Ios) { Json {} }
	val stonks = client.request<StonksResult>("http://wsbstonks.ddns.net:8085/api/stonks")
	return stonks.toString()
}
