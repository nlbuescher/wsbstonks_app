package net.ddns.wsbstonks.android

import android.os.*
import android.widget.*
import androidx.appcompat.app.*
import net.ddns.wsbstonks.shared.*

fun greet(): String {
	return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val tv: TextView = findViewById(R.id.text_view)
		tv.text = greet()
	}
}
