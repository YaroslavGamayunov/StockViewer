package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.ui.adapters.MainPageViewPagerAdapter

// TODO: Find out why data is not loaded on the first start of application
class MainPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO Rename mainPageSearchTextView
        view.findViewById<TextView>(R.id.mainPageSearchTextView).setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_stockSearchFragment)
        }
        view.findViewById<ImageView>(R.id.settingsImageButton).setOnClickListener {
            findNavController().navigate(R.id.action_mainPageFragment_to_settingsFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val viewPager = requireView().findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = requireView().findViewById<TabLayout>(R.id.tabLayout)
        val adapter = MainPageViewPagerAdapter(requireActivity())
        viewPager.adapter = adapter

        val tabTitles =
            requireActivity().resources.getStringArray(R.array.main_page_tab_titles)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            viewPager.setCurrentItem(tab.position, true)
        }.attach()

        viewPager.isUserInputEnabled = false
    }
}