package com.yaroslavgamayunov.stockviewer.ui.adapters

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.yaroslavgamayunov.stockviewer.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

class StockPriceMarkerView(
    context: Context,
    layoutResource: Int,
    var highTimeRes: Boolean = false
) :
    MarkerView(context, layoutResource) {
    private val priceTextView: TextView = findViewById(R.id.stockPriceTextView)
    private val priceDateTextView: TextView = findViewById(R.id.stockPriceDateTextView)

    private var currentEntry: Entry? = null

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            currentEntry = it
            priceTextView.text = "$${it.y}"
            val time = (it.x.toDouble() * 1000).roundToLong()
            val timeFormat = if (highTimeRes) "d MMM yyyy HH:mm" else "d MMM yyyy"
            priceDateTextView.text = SimpleDateFormat(timeFormat, Locale.US).format(Date(time))
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height - 15).toFloat())
    }
}