package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.yaroslavgamayunov.stockviewer.R

class StockDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_detail, container, false)
    }

    lateinit var toolbar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isFavourite = true
        toolbar = view.findViewById<Toolbar>(R.id.stockDetailFragmentToolbar).apply {
            setNavigationOnClickListener {
                view.findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                return@setOnMenuItemClickListener when (it.itemId) {
                    R.id.actionFavourites -> {
                        setFavourite(isFavourite)
                        isFavourite = !isFavourite
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setFavourite(isFavourite: Boolean) {
        val drawableId =
            if (isFavourite) R.drawable.ic_round_star_24 else R.drawable.ic_round_star_border_24
        toolbar.menu.findItem(R.id.actionFavourites).setIcon(drawableId)

    }
}