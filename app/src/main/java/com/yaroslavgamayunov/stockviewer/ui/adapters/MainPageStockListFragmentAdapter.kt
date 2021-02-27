package com.yaroslavgamayunov.stockviewer.ui.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yaroslavgamayunov.stockviewer.ui.StockListFragment

class MainPageStockListFragmentAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = 5

    override fun createFragment(position: Int) = StockListFragment()

}