package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.google.android.material.color.MaterialColors
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockPriceMarkerView
import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData

class StockChartFragment : Fragment() {
    lateinit var chart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chart = view.findViewById(R.id.stockDetailChart)
        setupChart()
    }

    private fun setupChart() {
        // These 3 lines remove grid
        chart.xAxis.isEnabled = false
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false

        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        // To fit the whole width
        chart.minOffset = 0f


        // For debug only
        val prices = resources.getStringArray(R.array.apple_prices)
            .map { it.toDouble() }
        val times = (1..prices.size).map { it.toLong() }

        val markerView = StockPriceMarkerView(
            requireActivity(),
            R.layout.marker_stock_chart
        )

        // Set the marker to the chart
        markerView.chartView = chart
        chart.marker = markerView

        setChartData(HistoricalCandleData(prices, prices, prices, prices, times))
    }

    private fun setChartData(data: HistoricalCandleData) {
        val entries = data.timeStamps.zip(data.closePrices)
            .map { (timeStamp, closePrice) -> Entry(timeStamp.toFloat(), closePrice.toFloat()) }
        val dataset = LineDataSet(entries, "")
        dataset.setDrawFilled(true)
        dataset.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

        val drawable =
            ContextCompat.getDrawable(requireActivity(), R.drawable.gradient_chart_background)
        dataset.fillDrawable = drawable


        // Smoothing linechart
        dataset.lineWidth = 2f
        dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
        //to enable the cubic density : if 1 then it will be sharp curve

        dataset.cubicIntensity = 0.2f

        // Highlight
        dataset.color = MaterialColors.getColor(chart, R.attr.colorSecondary)
        dataset.highLightColor = MaterialColors.getColor(chart, R.attr.colorSecondaryVariant)

        dataset.setDrawHorizontalHighlightIndicator(false)

        // removing points
        dataset.setDrawCircles(false)
        dataset.enableDashedHighlightLine(15f, 5f, 0f)
        dataset.highlightLineWidth = 1.5f

        // To fit the whole width
        chart.minOffset = 0f

        chart.data = LineData(dataset).also { it.isHighlightEnabled = true }
    }

    companion object {
        private const val STOCK_TICKER_TAG = "stock_ticker"

        fun newInstance(ticker: String): StockChartFragment {
            return StockChartFragment().apply {
                arguments = bundleOf(STOCK_TICKER_TAG to ticker)
            }
        }
    }
}