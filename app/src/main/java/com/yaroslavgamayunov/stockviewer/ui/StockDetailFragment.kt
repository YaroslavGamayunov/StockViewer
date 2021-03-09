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
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository

class StockDetailFragment : Fragment() {

    val args: StockDetailFragmentArgs by navArgs()
    lateinit var stockViewModel: StockViewModel

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
                stockViewModel.favourite(args.ticker)
            } else {
                stockViewModel.unfavourite(args.ticker)
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
            StockApiRepository(
                IexCloudApiService.create(),
                FinHubApiService.create(),
                StockDatabase.getInstance(requireActivity().applicationContext)
            )
        )

        stockViewModel =
            ViewModelProvider(
                requireActivity(),
                factory
            ).get(StockViewModel::class.java)

        lifecycleScope.launchWhenCreated {
            isFavourite = stockViewModel.isFavourite(args.ticker)
        }
    }
}