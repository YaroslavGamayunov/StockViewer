package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockListFragment : Fragment() {

    lateinit var stockViewModel: StockViewModel

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
        stockViewModel =
            ViewModelProvider(
                requireActivity(),
                factory
            ).get(StockViewModel::class.java)

        setupList()
    }

    private fun setupList() {
        listAdapter = StockListAdapter { stockItem ->
            findNavController().navigate(R.id.stockDetailFragment)
        }
        recyclerView.adapter = listAdapter

        lifecycleScope.launch {
            stockViewModel.getStocksForIndex("^NDX").collectLatest {
                listAdapter.submitData(it)
            }
        }
    }
}