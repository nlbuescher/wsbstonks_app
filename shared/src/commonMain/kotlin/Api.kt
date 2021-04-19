package net.ddns.wsbstonks.shared

import kotlin.coroutines.*

data class Stonk(
	val symbol: String,
	val name: String,
	val count: Int,
	val buyin: Float,
	val price: Float,
	val currency: String,
)

class Api(private val socket: Socket) {
	companion object {
		private const val allStonks = "allstonks"
	}

	suspend fun allStonks(): List<Stonk> = suspendCoroutine {
		socket.once(allStonks) { stonks ->
			println(stonks.first())
			it.resumeWith(Result.success(emptyList()))
		}
		socket.emit(allStonks)
	}
}
