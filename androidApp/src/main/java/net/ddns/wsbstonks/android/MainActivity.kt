package net.ddns.wsbstonks.android

import android.os.*
import android.widget.*
import androidx.appcompat.app.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import net.ddns.wsbstonks.shared.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
	lateinit var text: TextView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		text = findViewById(R.id.text_view)
	}

	override fun onResume() {
		super.onResume()
		launch(Main) {
			val stonks = withContext(IO) {
				Api.allStonks()
			}
			text.text = "${getRelativeTime(stonks.timestamp)}\n${stonks.stonks}"
		}
	}
}
