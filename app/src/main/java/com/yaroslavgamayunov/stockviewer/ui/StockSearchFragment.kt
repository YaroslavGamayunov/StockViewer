package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockSearchViewModel
import com.yaroslavgamayunov.stockviewer.model.StockSearchViewModel.SearchState
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockChipsAdapter
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockListAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockSearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var stockSearchViewModel: StockSearchViewModel

    private lateinit var searchListAdapter: StockListAdapter
    private lateinit var popularTickersAdapter: StockChipsAdapter

    private lateinit var searchMotionLayout: MotionLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<SearchView>(R.id.stockSearchView)
            .setOnQueryTextListener(this@StockSearchFragment)

        view.findViewById<View>(R.id.stockSearchFragmentBackButton).setOnClickListener {
            findNavController().navigateUp()
        }

        searchMotionLayout = view.findViewById(R.id.stockSearchMotionLayout)

        searchListAdapter = StockListAdapter(onItemClick = { stockItem ->
            //TODO: Find out how to handle other fragments (if current destination is not just a MainPageFragment)
            val action = MainPageFragmentDirections
                .actionMainPageFragmentToStockDetailsFragment(stockItem.ticker)
            findNavController().navigate(action)
        })
        view.findViewById<RecyclerView>(R.id.searchResultsRecyclerView).adapter = searchListAdapter

        popularTickersAdapter = StockChipsAdapter()
        view.findViewById<RecyclerView>(R.id.popularTickersRecyclerView).apply {
            adapter = popularTickersAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        }
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
        stockSearchViewModel =
            ViewModelProvider(
                requireActivity(),
                factory
            ).get(StockSearchViewModel::class.java)

        fillPopularTickersList()
        setupSearchList()
    }

    private fun setupSearchList() {
        requireView().findViewById<SearchView>(R.id.stockSearchView)?.apply {
            // Hack to make keyboard show up
            isIconified = true
            isIconified = false
        }

        lifecycleScope.launchWhenResumed {
            stockSearchViewModel.searchState.collect {
                when (it) {
                    is SearchState.OnQuery -> searchMotionLayout.transitionToState(R.id.nonEmptyInput)
                    is SearchState.Empty -> searchMotionLayout.transitionToState(R.id.emptyInput)
                }
            }
        }
    }

    private fun fillPopularTickersList() {
        lifecycleScope.launchWhenResumed {
            val popular = stockSearchViewModel.popularTickers(20)
            popularTickersAdapter.submitList(popular)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = performSearch(query)

    override fun onQueryTextChange(newText: String?): Boolean = performSearch(newText)

    private fun performSearch(query: String?): Boolean {
        lifecycleScope.launch {
            stockSearchViewModel.search(query).collectLatest {
                searchListAdapter.submitData(it)
            }
        }

        return true
    }
}