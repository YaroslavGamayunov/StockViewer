package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.ui.adapters.MainPageStockListFragmentAdapter
import kotlin.math.min

class MainPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewPager = requireView().findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = requireView().findViewById<TabLayout>(R.id.tabLayout)
        val adapter = MainPageStockListFragmentAdapter(requireActivity())
        viewPager.adapter = adapter

        val tabTitles =
            requireActivity().resources.getStringArray(R.array.main_page_tab_titles)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            viewPager.setCurrentItem(min(tab.position, 5), true)
        }.attach()

        viewPager.isUserInputEnabled = false
    }
}