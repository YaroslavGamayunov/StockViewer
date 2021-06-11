package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.StockViewerApplication
import com.yaroslavgamayunov.stockviewer.model.StockDatabaseViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockListAdapter
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockListFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockListFragment : Fragment() {
    @Inject
    lateinit var stockViewModelFactory: StockViewModelFactory

    private val stockDatabaseViewModel: StockDatabaseViewModel by viewModels { stockViewModelFactory }

    lateinit var recyclerView: RecyclerView
    lateinit var listAdapter: StockListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.stockListRecyclerView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (requireActivity().application as StockViewerApplication)
            .repositoryComponent.inject(this)

        setupList(arguments)
    }

    private fun setupList(arguments: Bundle?) {
        val filter: StockListFilter =
            arguments?.get(LIST_FILTER_TAG) as? StockListFilter ?: StockListFilter.ALL

        listAdapter = StockListAdapter(
            onItemClick = { stockItem ->
                val action = MainPageFragmentDirections
                    .actionMainPageFragmentToStockDetailsFragment(stockItem.ticker)
                findNavController().navigate(action)
            })

        recyclerView.adapter = listAdapter


        val data = lifecycleScope.async(Dispatchers.IO) {
            when (filter) {
                StockListFilter.ALL ->
                    stockDatabaseViewModel.getStocksForIndexPaged("^NDX")

                StockListFilter.FAVOURITES ->
                    stockDatabaseViewModel.getFavouriteStocksPaged()
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            data.await().collectLatest { pagingData ->
                withContext(Dispatchers.Main) {
                    listAdapter.submitData(pagingData)
                }
            }
        }
    }

    companion object {
        private const val LIST_FILTER_TAG = "list_filter"

        fun newInstance(filter: StockListFilter): StockListFragment {
            return StockListFragment().apply {
                arguments = bundleOf(LIST_FILTER_TAG to filter)
            }
        }
    }
}