package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.*

@Serializable
data class Portfolio(
	val buyin: Float,
	val value: Float,
	val diffEuro: Float,
	val diffPercent: Float,
)

@Serializable
data class Stonk(
	val symbol: String,
	val name: String,
	val count: Int,
	val buyinPrice: Float,
	val buyinValue: Float,
	val currentPrice: Float,
	val currentValue: Float,
	val diffEuro: Float,
	val diffPercent: Float,
)

@Serializable
data class StonksResult(
	val timestamp: Long,
	val portfolio: Portfolio,
	val stonks: List<Stonk>,
)

@HttpClientDsl
expect fun HttpClient(block: HttpClientConfig<*>.() -> Unit = {}): HttpClient

object Api {
	private const val SERVER = "https://wsbstonks.info"
	private val client = HttpClient {
		install(JsonFeature) {
			serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
				ignoreUnknownKeys = true
			})
		}
	}

	suspend fun allStonks(): StonksResult = try {
		client.request("$SERVER/api/stonks")
	} catch(e: Exception) {
		println(e)
		throw e
	}
}
