package net.ddns.wsbstonks.android

import android.content.*
import android.graphics.*
import android.os.*
import android.view.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import net.ddns.wsbstonks.android.databinding.*
import net.ddns.wsbstonks.shared.*
import java.util.*
import kotlin.concurrent.*

class StonksAdapter(private val context: Context) : RecyclerView.Adapter<StonksAdapter.ViewHolder>() {
	var stonks = emptyList<Stonk>()

	class ViewHolder(val binding: StonkItemBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(StonkItemBinding.inflate(LayoutInflater.from(parent.context)))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val stonk = stonks[position]
		val color = if (stonk.diffEuro < 0) Color.RED else Color.GREEN
		with(holder.binding) {
			symbol.text = stonk.symbol
			count.text = context.getString(R.string.count, stonk.count)
			name.text = stonk.name
			buyinPrice.text = context.getString(R.string.euros, localNumber(stonk.buyinPrice, 3))
			buyinValue.text = context.getString(R.string.euros, localNumber(stonk.buyinValue, 2))
			with(currentPrice) {
				text = context.getString(R.string.euros, localNumber(stonk.currentPrice, 3))
				setTextColor(color)
			}
			with(currentValue) {
				text = context.getString(R.string.euros, localNumber(stonk.currentValue, 2))
				setTextColor(color)
			}
			with(diffEuros) {
				text = context.getString(R.string.euros, localNumber(stonk.diffEuro, 2))
				setTextColor(color)
			}
			with(diffPercent) {
				text = context.getString(R.string.percent, localNumber(stonk.diffPercent, 2))
				setTextColor(color)
			}
		}
	}

	override fun getItemCount(): Int = stonks.size
}

class MainViewModel : ViewModel() {
	val timeString = MutableLiveData<String>()
	val portfolio = MutableLiveData<Portfolio>()
	val stonks = MutableLiveData<List<Stonk>>()

	suspend fun loadData() = withContext(IO) {
		val data = Api.allStonks()
		timeString.postValue(relativeTime(data.timestamp))
		portfolio.postValue(data.portfolio)
		stonks.postValue(data.stonks)
	}
}

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
	private val viewModel: MainViewModel by viewModels()

	private lateinit var binding: ActivityMainBinding
	private var timer: Timer? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val stonksAdapter = StonksAdapter(this)
		binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
		binding.recyclerView.adapter = stonksAdapter

		viewModel.timeString.observe(this) {
			binding.elapsedTime.text = it
		}
		viewModel.portfolio.observe(this) {
			val color = if (it.diffEuro < 0) Color.RED else Color.GREEN
			with(binding.buyin) {
				text = localNumber(it.buyin, 2)
			}
			with(binding.value) {
				text = getString(R.string.euros, localNumber(it.value, 2))
				setTextColor(color)
			}
			with(binding.diffEuros) {
				text = getString(R.string.euros, localNumber(it.diffEuro, 2))
				setTextColor(color)
			}
			with(binding.diffPercent) {
				text = getString(R.string.percent, localNumber(it.diffPercent, 2))
				setTextColor(color)
			}
		}
		viewModel.stonks.observe(this) {
			stonksAdapter.stonks = it
			stonksAdapter.notifyDataSetChanged()
		}
	}

	override fun onResume() {
		super.onResume()
		timer = fixedRateTimer(period = 10_000) {
			launch { viewModel.loadData() }
		}
	}

	override fun onPause() {
		super.onPause()
		timer?.cancel()
		timer = null
	}
}
