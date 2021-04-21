package net.ddns.wsbstonks.shared

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.*

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

expect fun HttpClient(): HttpClient

object Api {
	private const val SERVER = "http://wsbstonks.ddns.net:8085/api"
	private val client = HttpClient()

	suspend fun allStonks(): StonksResult = client.request("$SERVER/stonks")
}

expect fun getRelativeTime(fromTimestamp: Long): String
