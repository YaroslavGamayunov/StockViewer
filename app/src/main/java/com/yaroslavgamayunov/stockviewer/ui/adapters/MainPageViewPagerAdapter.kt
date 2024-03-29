package com.yaroslavgamayunov.stockviewer.ui.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yaroslavgamayunov.stockviewer.ui.StockListFragment

class MainPageViewPagerAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = StockListFilter.values().size

    override fun createFragment(position: Int) =
        StockListFragment.newInstance(StockListFilter.values()[position])

}