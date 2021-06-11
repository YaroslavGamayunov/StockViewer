package com.yaroslavgamayunov.stockviewer.repository

import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.utils.CallResult
import com.yaroslavgamayunov.stockviewer.utils.safeApiCall
import com.yaroslavgamayunov.stockviewer.vo.CompanyInfo
import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData
import com.yaroslavgamayunov.stockviewer.vo.NewsItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class StockApiRepository @Inject constructor(
    private val iexCloudApiService: IexCloudApiService,
    private val finHubApiService: FinHubApiService
) {
    suspend fun getHistoricalData(
        ticker: String,
        duration: StockDataDuration
    ): CallResult<HistoricalCandleData> {
        return safeApiCall {
            finHubApiService.getHistoricalData(
                ticker,
                duration.resolution,
                duration.startTime,
                duration.endTime
            )
        }
    }

    suspend fun getNews(
        ticker: String,
        startTime: Long,
        endTime: Long
    ): CallResult<List<NewsItem>> {
        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(startTime))
        val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(endTime))
        return safeApiCall {
            finHubApiService.getNews(ticker, startDate, endDate)
        }
    }

    suspend fun getCompanyInfo(ticker: String): CallResult<CompanyInfo> {
        return safeApiCall {
            iexCloudApiService.getCompanyInfo(ticker)
        }
    }
}

sealed class StockDataDuration {
    val endTime: Long
        get() {
            val calendar = Calendar.getInstance()
            return calendar.time.time.div(1000)
        }
    val startTime: Long
        get() {
            val calendar = Calendar.getInstance()
            when (this) {
                is All -> return 0
                is Day -> calendar.add(Calendar.DAY_OF_YEAR, -1)
                is HalfYear -> calendar.add(Calendar.MONTH, -6)
                is Month -> calendar.add(Calendar.MONTH, -1)
                is Week -> calendar.add(Calendar.WEEK_OF_YEAR, -1)
                is Year -> calendar.add(Calendar.YEAR, -1)
            }
            return calendar.time.time.div(1000)
        }

    val resolution: String
        get() {
            return when (this) {
                is All -> "W"
                is Day -> "30"
                is HalfYear -> "D"
                is Month -> "D"
                is Week -> "60"
                is Year -> "D"
            }
        }

    object Day : StockDataDuration()

    object Week : StockDataDuration()

    object Month : StockDataDuration()

    object HalfYear : StockDataDuration()

    object Year : StockDataDuration()

    object All : StockDataDuration()
}