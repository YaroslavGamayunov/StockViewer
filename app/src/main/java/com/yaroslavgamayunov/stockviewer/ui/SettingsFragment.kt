package com.yaroslavgamayunov.stockviewer.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.StockViewerApplication
import com.yaroslavgamayunov.stockviewer.databinding.FragmentSettingsBinding
import com.yaroslavgamayunov.stockviewer.di.DaggerSettingsComponent
import com.yaroslavgamayunov.stockviewer.di.SettingsComponent

class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null
    private lateinit var settingsComponent: SettingsComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        val toolbar = view.findViewById<Toolbar>(R.id.settingsFragmentToolbar)
        toolbar.setupWithNavController(findNavController())

        binding!!.newsPublisherContactInformation.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_newsPublisherContactFragment)
        }

        binding!!.applicationInfoTextView.setOnClickListener {
            val uri = settingsComponent.getAppInfoPageUri()
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val application = requireActivity().application as StockViewerApplication
        settingsComponent =
            DaggerSettingsComponent.builder().appComponent(application.appComponent).build()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}