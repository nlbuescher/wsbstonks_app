package net.ddns.wsbstonks.shared

class Greeting {
	fun greeting(): String {
		return "Hello, ${Platform().platform}!"
	}
}
