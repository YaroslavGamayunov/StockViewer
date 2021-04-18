package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.databinding.FragmentCompanyInfoBinding
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockApiViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.ui.adapters.ChipListAdapter
import com.yaroslavgamayunov.stockviewer.vo.CompanyInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CompanyInfoFragment : Fragment() {
    private lateinit var stockApiViewModel: StockApiViewModel
    private var binding: FragmentCompanyInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCompanyInfoBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = StockViewModelFactory(
            IexCloudApiService.create(),
            FinHubApiService.create(),
            StockDatabase.getInstance(requireActivity().applicationContext)
        )

        stockApiViewModel =
            ViewModelProvider(
                requireActivity(),
                factory
            ).get(StockApiViewModel::class.java)

        arguments?.getString(STOCK_TICKER_TAG)?.let { loadCompanyInfo(it) }
    }

    private fun updateCompanyInfoLayout(companyInfo: CompanyInfo) {
        binding!!.companyName.text = companyInfo.companyName
        binding!!.employeesTextView.text = companyInfo.employees.toString()
        binding!!.addressTextView.apply {
            val address = companyInfo.getAddressString()
            text = address
            visibility = if (address.isEmpty()) View.GONE else View.VISIBLE
        }
        binding!!.companyDescriptionTextView.text = companyInfo.description
        binding!!.companyTagRecyclerView.adapter =
            ChipListAdapter(onClick = {}).also { it.submitList(companyInfo.tags) }
        with(binding!!.companyWebsiteTextView) {
            text = companyInfo.website
            // TODO: Make links clickable
            //Linkify.addLinks(this, Linkify.MAP_ADDRESSES)
        }
    }

    private fun loadCompanyInfo(ticker: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                binding!!.companyInfoConstraintLayout.visibility = View.INVISIBLE
                binding!!.progressBar.visibility = View.VISIBLE
            }

            val companyInfo = stockApiViewModel.getCompanyInfo(ticker)

            withContext(Dispatchers.Main) {
                updateCompanyInfoLayout(companyInfo)
                binding!!.companyInfoConstraintLayout.visibility = View.VISIBLE
                binding!!.progressBar.visibility = View.INVISIBLE
            }
        }
    }


    companion object {
        private const val STOCK_TICKER_TAG = "stock_ticker"

        fun newInstance(ticker: String): CompanyInfoFragment {
            return CompanyInfoFragment().apply {
                arguments = bundleOf(STOCK_TICKER_TAG to ticker)
            }
        }
    }
}