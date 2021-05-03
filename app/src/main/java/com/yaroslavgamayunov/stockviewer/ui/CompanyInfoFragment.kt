package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.StockViewerApplication
import com.yaroslavgamayunov.stockviewer.databinding.FragmentCompanyInfoBinding
import com.yaroslavgamayunov.stockviewer.model.StockApiViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.ui.adapters.ChipListAdapter
import com.yaroslavgamayunov.stockviewer.utils.CallResult
import com.yaroslavgamayunov.stockviewer.vo.CompanyInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CompanyInfoFragment : Fragment() {
    @Inject
    lateinit var stockViewModelFactory: StockViewModelFactory

    private val stockApiViewModel: StockApiViewModel by viewModels { stockViewModelFactory }
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

        (requireActivity().application as StockViewerApplication)
            .repositoryComponent.inject(this)

        arguments?.getString(STOCK_TICKER_TAG)?.let { loadCompanyInfo(it) }
    }

    private fun updateCompanyInfoLayout(companyInfo: CompanyInfo) = binding?.apply {
        companyName.text = companyInfo.companyName
        employeesTextView.text = companyInfo.employees.toString()
        addressTextView.apply {
            val address = companyInfo.getAddressString()
            text = address
            visibility = if (address.isEmpty()) View.GONE else View.VISIBLE
        }
        companyDescriptionTextView.text = companyInfo.description
        companyTagRecyclerView.adapter =
            ChipListAdapter(onClick = {}).also { it.submitList(companyInfo.tags) }

        with(companyWebsiteTextView) {
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

            when (companyInfo) {
                is CallResult.Error -> tryToReloadCompanyInfo(ticker)

                is CallResult.Success -> withContext(Dispatchers.Main) {
                    updateCompanyInfoLayout(companyInfo.value!!)
                    binding!!.companyInfoConstraintLayout.visibility = View.VISIBLE
                    binding!!.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun tryToReloadCompanyInfo(ticker: String) {
        if (activity is MainActivity) {
            (activity as MainActivity).showRetrySnackbar(R.string.message_unstable_internet) {
                loadCompanyInfo(ticker)
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