import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockApiViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockNewsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CompanyNewsFragment : Fragment() {
    lateinit var stockApiViewModel: StockApiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company_news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = StockViewModelFactory(
            IexCloudApiService.create(),
            FinHubApiService.create(),
            StockDatabase.getInstance(requireActivity().applicationContext)
        )

        stockApiViewModel =
            ViewModelProvider(
                requireActivity(),
                factory
            ).get(StockApiViewModel::class.java)
        setupNewsList()
    }

    private fun setupNewsList() {
        val newsRecyclerView = requireView().findViewById<RecyclerView>(R.id.stockNewsRecyclerView)
        val adapter = StockNewsAdapter()
        newsRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val ticker = requireArguments().getString(STOCK_TICKER_TAG)!!
            val calendar = Calendar.getInstance()
            val endTime = calendar.time.time
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            val startTime = calendar.time.time

            val news = stockApiViewModel.getNews(ticker, startTime, endTime)

            withContext(Dispatchers.Main) {
                adapter.submitList(news)
            }
        }
    }

    companion object {
        private const val STOCK_TICKER_TAG = "stock_ticker"

        fun newInstance(ticker: String): CompanyNewsFragment {
            return CompanyNewsFragment().apply {
                arguments = bundleOf(STOCK_TICKER_TAG to ticker)
            }
        }
    }
}