package net.ddns.wsbstonks.shared

interface Socket {
	fun on(event: String, callback: (Array<Any>) -> Unit)
	fun once(event: String, callback: (Array<Any>) -> Unit)
	fun emit(event: String, vararg args: Any)
}
