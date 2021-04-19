package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlin.test.*

@Serializable
data class Stonk(
	val id: Int,
	val symbol: String,
	val name: String,
	@SerialName("menge")
	val count: Int,
	val buyin: Float,
	@SerialName("kurs")
	val price: Float,
	val currency: String,
)

@Serializable
data class StonksResult(
	val timestamp: Long,
	val stonks: List<Stonk>,
)

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
