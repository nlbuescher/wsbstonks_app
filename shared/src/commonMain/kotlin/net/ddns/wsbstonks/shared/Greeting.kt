package net.ddns.wsbstonks.shared

class Api(private val socketConstructor: () -> Socket) {
	fun Socket() = socketConstructor()
}

class Greeting {
	fun greeting(): String {
		return "Hello, ${Platform().platform}!"
	}
}
