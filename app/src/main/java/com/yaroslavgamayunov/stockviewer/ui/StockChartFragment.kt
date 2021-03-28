package com.yaroslavgamayunov.stockviewer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.google.android.material.color.MaterialColors
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.databinding.FragmentStockChartBinding
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.model.StockViewModel
import com.yaroslavgamayunov.stockviewer.model.StockViewModelFactory
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.repository.StockDataDuration
import com.yaroslavgamayunov.stockviewer.ui.adapters.StockPriceMarkerView
import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockChartFragment : Fragment() {
    private var binding: FragmentStockChartBinding? = null
    private lateinit var stockViewModel: StockViewModel
    private lateinit var marker: StockPriceMarkerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stock_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentStockChartBinding.bind(view)
        setupChart()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
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
    }

    private fun setupChart() {
        val chart = binding!!.stockDetailChart
        // These 3 lines remove grid
        chart.xAxis.isEnabled = false
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false

        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        // To fit the whole width
        chart.minOffset = 0f


        marker = StockPriceMarkerView(
            requireActivity(),
            R.layout.marker_stock_chart
        )

        // Set the marker to the chart
        marker.chartView = chart
        chart.marker = marker

        val chartPaint = chart.getPaint(Chart.PAINT_INFO);
        chartPaint.textSize = 30f
        chartPaint.color = MaterialColors.getColor(chart, R.attr.colorSecondary)

        val ticker = requireArguments().getString(STOCK_TICKER_TAG)!!

        binding!!.stockChartDayButton.setOnClickListener {
            loadAndSetChartData(ticker, StockDataDuration.Day)
        }

        binding!!.stockChartWeekButton.setOnClickListener {
            loadAndSetChartData(ticker, StockDataDuration.Week)
        }

        binding!!.stockChartMonthButton.setOnClickListener {
            loadAndSetChartData(ticker, StockDataDuration.Month)
        }

        binding!!.stockChartHalfYearButton.setOnClickListener {
            loadAndSetChartData(ticker, StockDataDuration.HalfYear)
        }

        binding!!.stockChartYearButton.setOnClickListener {
            loadAndSetChartData(ticker, StockDataDuration.Year)
        }

        binding!!.stockChartAllButton.setOnClickListener {
            loadAndSetChartData(ticker, StockDataDuration.All)
        }
    }

    private fun loadAndSetChartData(
        ticker: String,
        duration: StockDataDuration
    ) {
        marker.highTimeRes = duration is StockDataDuration.Day

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                binding!!.progressBar.visibility = View.VISIBLE
                binding!!.stockDetailChart.visibility = View.INVISIBLE
            }

            val data = stockViewModel.getHistoricalCandleData(
                ticker,
                duration
            )

            withContext(Dispatchers.Main) {
                binding!!.progressBar.visibility = View.INVISIBLE
                binding!!.stockDetailChart.visibility = View.VISIBLE
                setChartData(data)
            }
        }
    }

    private fun setChartData(data: HistoricalCandleData?) {
        Log.d("chart", "setting data: $data")
        val chart = binding!!.stockDetailChart
        if (data?.closePrices == null || data.timeStamps == null) {
            chart.clear()
            return
        }

        val entries = data.timeStamps.zip(data.closePrices)
            .map { (timeStamp, closePrice) ->
                Entry(timeStamp.toFloat(), closePrice.toFloat())
            }
        val dataSet = LineDataSet(entries, "")
        dataSet.setDrawFilled(true)
        dataSet.fillFormatter =
            IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }

        val drawable =
            ContextCompat.getDrawable(requireActivity(), R.drawable.gradient_chart_background)
        dataSet.fillDrawable = drawable


        // Smoothing linechart
        dataSet.lineWidth = 2f
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        //to enable the cubic density : if 1 then it will be sharp curve

        dataSet.cubicIntensity = 0.2f

        // Highlight
        dataSet.color = MaterialColors.getColor(chart, R.attr.colorSecondary)
        dataSet.highLightColor = MaterialColors.getColor(chart, R.attr.colorSecondaryVariant)

        dataSet.setDrawValues(false)

        dataSet.setDrawHorizontalHighlightIndicator(false)

        // removing points
        dataSet.setDrawCircles(false)
        dataSet.enableDashedHighlightLine(15f, 5f, 0f)
        dataSet.highlightLineWidth = 1.5f

        // To fit the whole width
        chart.minOffset = 0f

        chart.extraTopOffset = 32f
        chart.data = LineData(dataSet).also { it.isHighlightEnabled = true }
        chart.notifyDataSetChanged()
        chart.animateY(2000, Easing.EaseOutBack)
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