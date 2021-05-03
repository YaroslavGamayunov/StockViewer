package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.StockViewerApplication
import com.yaroslavgamayunov.stockviewer.model.StockApiViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockNewsAdapter
import com.yaroslavgamayunov.stockviewer.utils.CallResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class CompanyNewsFragment : Fragment() {
    @Inject
    lateinit var stockViewModelFactory: StockViewModelFactory

    private val stockApiViewModel: StockApiViewModel by viewModels { stockViewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company_news, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (requireActivity().application as StockViewerApplication)
            .repositoryComponent.inject(this)

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
                if (news is CallResult.Success)
                    adapter.submitList(news.value)
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