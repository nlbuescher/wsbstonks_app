package net.ddns.wsbstonks.shared

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
