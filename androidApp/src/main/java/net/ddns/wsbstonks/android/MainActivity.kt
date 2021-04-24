package net.ddns.wsbstonks.android

import android.os.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import net.ddns.wsbstonks.android.databinding.*
import net.ddns.wsbstonks.shared.*
import kotlin.concurrent.*

class MainViewModel : ViewModel() {
	val timeString = MutableLiveData<String?>()
	val portfolio = MutableLiveData<Portfolio?>()
	val stonks = MutableLiveData<List<Stonk>?>()

	suspend fun loadData() = withContext(IO) {
		val data = Api.allStonks()
		timeString.postValue(relativeTime(data.timestamp))
		portfolio.postValue(data.portfolio)
		stonks.postValue(data.stonks)
	}
}

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
	private val binding = ActivityMainBinding.inflate(layoutInflater)
	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}

	override fun onResume() {
		super.onResume()
		fixedRateTimer(period = 10_000) {
			launch { viewModel.loadData() }
		}
	}
}
