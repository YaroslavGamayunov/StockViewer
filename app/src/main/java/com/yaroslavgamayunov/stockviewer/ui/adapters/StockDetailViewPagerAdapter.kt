package com.yaroslavgamayunov.stockviewer.ui.adapters

import CompanyNewsFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yaroslavgamayunov.stockviewer.ui.StockChartFragment

class StockDetailViewPagerAdapter(activity: FragmentActivity, val ticker: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = StockDetailTabType.values().size

    override fun createFragment(position: Int): Fragment {
        val tabType = StockDetailTabType.values()[position]
        return when (tabType) {
            StockDetailTabType.CHART -> {
                StockChartFragment.newInstance(ticker)
            }
            StockDetailTabType.NEWS -> {
                CompanyNewsFragment.newInstance(ticker)
            }
        }
    }

}