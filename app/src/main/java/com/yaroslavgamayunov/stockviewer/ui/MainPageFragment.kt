package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.marginLeft
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
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
        val texts = arrayOf("Stocks", "Favourite", "News", "Forecasts", "Summary")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = texts[position]
            viewPager.setCurrentItem(min(tab.position, 5), true)
        }.attach()

        viewPager.isUserInputEnabled = false

//        for (i in 0 until tabLayout.tabCount) {
//            val tab = tabLayout.getTabAt(i)
//            tab?.let {
//                val view = layoutInflater.inflate(R.layout.tab_item_title, tabLayout, false)
//                (view.findViewById<MaterialTextView>(R.id.tabItemText)).text = it.text
//                it.setCustomView(view)
//            }
//        }
//
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                tab?.let {
//                    (it.view.findViewById<MaterialTextView>(R.id.tabItemText)).textSize = 25F
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                tab?.let {
//                    (it.view.findViewById<MaterialTextView>(R.id.tabItemText)).textSize = 18F
//                }
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                tab?.let {
//                    (it.view.findViewById<MaterialTextView>(R.id.tabItemText)).textSize = 25F
//                }
//            }
//
//
//        })
    }
}