package com.yaroslavgamayunov.stockviewer.ui.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.card.MaterialCardView
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.vo.StockItem


class StockListAdapter(
    val onItemClickListener: (StockItem) -> Unit
) :
    PagingDataAdapter<StockItem, StockListAdapter.ViewHolder>(ItemComparator) {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tickerTextView: TextView =
            itemView.findViewById(R.id.stockItemTickerTextView)

        private val companyNameTextView: TextView =
            itemView.findViewById(R.id.stockItemCompanyNameTextView)

        private val companyLogoImageView: ImageView =
            itemView.findViewById(R.id.stockItemCompanyLogoImageView)

        private val stockItemPriceTextView: TextView =
            itemView.findViewById(R.id.stockItemPriceTextView)

        fun onClick(clickListener: (Int) -> Unit) {
            itemView.setOnClickListener {
                clickListener.invoke(bindingAdapterPosition)
            }
        }

        private fun bindLogo(url: String) {
            val cornerRadius =
                itemView.context.resources.getDimension(R.dimen.stockItemCardCornerRadius)

            companyLogoImageView.load(url) {
                placeholder(R.drawable.logo_placeholder)
                transformations(RoundedCornersTransformation(cornerRadius))
                crossfade(true)
            }
        }

        fun bind(item: StockItem?) {
            if (item != null) {
                tickerTextView.text = item.ticker
                companyNameTextView.text = item.name

                // TODO: Handle different currencies
                stockItemPriceTextView.text = "$${item.previousDayClosePrice}"

                bindLogo(item.logoUrl)
            } else {
                tickerTextView.text = ""
                companyNameTextView.text = ""
            }
        }
    }

    override fun getItemViewType(position: Int) = position % 2

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun setItemCardColor(card: MaterialCardView, viewType: Int) {
        val cardColorAttr = if (viewType == 0) R.attr.colorPrimaryVariant else R.attr.colorPrimary

        val colorValue = TypedValue()
        card.context.theme.resolveAttribute(
            cardColorAttr,
            colorValue,
            true
        )

        card.setCardBackgroundColor(colorValue.data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_stock, parent, false)
        setItemCardColor(view as MaterialCardView, viewType)

        val viewHolder = ViewHolder(view)

        viewHolder.onClick { adapterPosition ->
            getItem(adapterPosition)?.let { onItemClickListener(it) }
        }

        return viewHolder
    }

    object ItemComparator : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.ticker == newItem.ticker
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem == newItem
        }

    }
}