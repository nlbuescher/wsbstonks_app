package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlin.test.*

class ApiTests {
	@Test
	fun test() {
		val client = HttpClient(Android) {
			Json {}
		}

		runBlocking {
			val stonks = client.request<StonksResult>("https://wsbstonks.ddns.net:8085/api/stonks")
			println(stonks)
		}
	}
}
