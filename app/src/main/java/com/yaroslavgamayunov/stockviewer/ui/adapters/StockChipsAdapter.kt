package com.yaroslavgamayunov.stockviewer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.yaroslavgamayunov.stockviewer.R


class StockChipsAdapter : ListAdapter<String, StockChipsAdapter.ChipViewHolder>(ITEM_COMPARATOR) {
    class ChipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tickerChip: Chip = view.findViewById(R.id.stockTickerChip)
        fun bind(ticker: String) {
            tickerChip.text = ticker
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_stock_chip, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val ticker: String = getItem(position)
        holder.bind(ticker)
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }
}