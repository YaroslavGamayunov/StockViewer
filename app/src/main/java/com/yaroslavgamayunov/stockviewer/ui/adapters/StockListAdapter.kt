package com.yaroslavgamayunov.stockviewer.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.data.StockItem
import coil.load
import coil.transform.RoundedCornersTransformation

class StockListAdapter :
    PagingDataAdapter<StockItem, StockListAdapter.ViewHolder>(ItemDifferentiator) {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tickerTextView: TextView =
            itemView.findViewById(R.id.stockItemTickerTextView)

        private val companyNameTextView: TextView =
            itemView.findViewById(R.id.stockItemCompanyNameTextView)

        private val companyLogoImageView: ImageView =
            itemView.findViewById(R.id.companyLogoImageView)

        private fun bindLogo(url: String) {
            val cornerRadius =
                itemView.context.resources.getDimension(R.dimen.stockItemCardCornerRadius)

            companyLogoImageView.load(url) {
                transformations(RoundedCornersTransformation(cornerRadius))
                crossfade(true)
            }
        }


        fun bind(item: StockItem?) {
            if (item != null) {
                tickerTextView.text = item.company.ticker
                companyNameTextView.text = item.company.name
                bindLogo(item.company.logoUrl)
            } else {
                tickerTextView.text = ""
                companyNameTextView.text = ""
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_stock, parent, false)

        return ViewHolder(view)
    }

    object ItemDifferentiator : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.company == newItem.company
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem == newItem
        }

    }
}