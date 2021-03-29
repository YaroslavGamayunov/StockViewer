package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockDatabaseViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockListAdapter
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockListFilter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockListFragment : Fragment() {

    lateinit var stockDatabaseViewModel: StockDatabaseViewModel

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
        val factory = StockViewModelFactory(
            StockApiRepository(
                IexCloudApiService.create(),
                FinHubApiService.create(),
                StockDatabase.getInstance(requireActivity().applicationContext)
            )
        )
        stockDatabaseViewModel =
            ViewModelProvider(
                requireActivity(),
                factory
            ).get(StockDatabaseViewModel::class.java)

        setupList(arguments)
    }

    private fun setupList(arguments: Bundle?) {
        val filter = arguments?.get(LIST_FILTER_TAG) ?: StockListFilter.ALL

        listAdapter = StockListAdapter(
            onItemClick = { stockItem ->
                //TODO: Find out how to handle other fragments (if current destination is not just a MainPageFragment)
                val action = MainPageFragmentDirections
                    .actionMainPageFragmentToStockDetailsFragment(stockItem.ticker)
                findNavController().navigate(action)
            })

        recyclerView.adapter = listAdapter


        lifecycleScope.launch {
            when (filter) {
                StockListFilter.ALL ->
                    stockDatabaseViewModel.getStocksForIndexPaged("^NDX").collectLatest {
                        listAdapter.submitData(it)
                    }

                StockListFilter.FAVOURITES ->
                    stockDatabaseViewModel.getFavouriteStocksPaged().collectLatest {
                        listAdapter.submitData(it)
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