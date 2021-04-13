package net.ddns.wsbstonks.shared

import org.junit.*
import org.junit.Assert.*

class AndroidGreetingTest {
	@Test
	fun testExample() {
		assertTrue("Check Android is mentioned", Greeting().greeting().contains("Android"))
	}
}
