package com.yaroslavgamayunov.stockviewer.ui.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.vo.NewsItem
import java.text.SimpleDateFormat
import java.util.*

class StockNewsAdapter : ListAdapter<NewsItem, StockNewsAdapter.ViewHolder>(ITEM_COMPARATOR) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val newsSourceTextView: TextView = view.findViewById(R.id.newsSourceTextView)
        private val newsHeadlineTextView: TextView = view.findViewById(R.id.newsHeadlineTextView)
        private val newsSummaryTextView: TextView = view.findViewById(R.id.newsSummaryTextView)
        private val newsDateTextView: TextView = view.findViewById(R.id.newsDateTextView)
        private val newsImageView: ImageView = view.findViewById(R.id.newsImageView)

        private fun bindLogo(url: String) {
            val cornerRadius =
                itemView.context.resources.getDimension(R.dimen.newsImageCornerRadius)

            newsImageView.load(url) {
                placeholder(R.drawable.logo_placeholder)
                transformations(RoundedCornersTransformation(cornerRadius))
                crossfade(true)
            }
        }

        fun bind(item: NewsItem) {
            newsSourceTextView.text = item.source
            newsHeadlineTextView.text = item.headline
            newsSummaryTextView.text = item.summary
            newsDateTextView.text =
                SimpleDateFormat(
                    "EEE, d MMM HH:mm",
                    Locale.US
                ).format(Date(item.date * 1000))
            bindLogo(item.imageUrl)

            itemView.setOnClickListener {
                val uri = Uri.parse(item.newsUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(itemView.context, intent, null)
            }
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem) = oldItem == newItem

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem) =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}