package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockDatabaseViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockDetailViewPagerAdapter

class StockDetailFragment : Fragment() {

    val args: StockDetailFragmentArgs by navArgs()
    lateinit var stockDatabaseViewModel: StockDatabaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_detail, container, false)
    }

    lateinit var toolbar: Toolbar

    var isFavourite: Boolean = false
        set(value) {
            val drawableRes =
                if (value) R.drawable.ic_round_star_inactive_24 else R.drawable.ic_round_star_border_24
            toolbar.menu.findItem(R.id.actionFavourite).setIcon(drawableRes)
            if (value) {
                stockDatabaseViewModel.favourite(args.ticker)
            } else {
                stockDatabaseViewModel.unfavourite(args.ticker)
            }
            field = value
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById<Toolbar>(R.id.stockDetailFragmentToolbar).apply {
            setNavigationOnClickListener {
                view.findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                return@setOnMenuItemClickListener when (it.itemId) {
                    R.id.actionFavourite -> {
                        isFavourite = !isFavourite

                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = StockViewModelFactory(
            IexCloudApiService.create(),
            FinHubApiService.create(),
            StockDatabase.getInstance(requireActivity().applicationContext)
        )

        stockDatabaseViewModel =
            ViewModelProvider(
                requireActivity(),
                factory
            ).get(StockDatabaseViewModel::class.java)

        lifecycleScope.launchWhenCreated {
            isFavourite = stockDatabaseViewModel.isFavourite(args.ticker)
            val item = stockDatabaseViewModel.getStockItem(args.ticker)
            toolbar.title = item.ticker
            toolbar.subtitle = item.name
        }

        setupViewPager()
    }

    private fun setupViewPager() {
        val viewPager = requireView().findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = requireView().findViewById<TabLayout>(R.id.tabLayout)
        val adapter = StockDetailViewPagerAdapter(requireActivity(), args.ticker)
        viewPager.adapter = adapter

        val tabTitles =
            requireActivity().resources.getStringArray(R.array.stock_detail_tab_titles)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            viewPager.setCurrentItem(tab.position, true)
        }.attach()

        viewPager.isUserInputEnabled = false
    }
}