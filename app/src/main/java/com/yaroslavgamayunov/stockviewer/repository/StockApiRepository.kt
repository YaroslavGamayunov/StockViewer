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
import kotlin.time.Duration
import kotlin.time.days

class StockApiRepository @Inject constructor(
    private val iexCloudApiService: IexCloudApiService,
    private val finHubApiService: FinHubApiService
) {
    suspend fun getHistoricalData(
        ticker: String,
        interval: StockDataInterval
    ): CallResult<HistoricalCandleData> {
        return safeApiCall {
            finHubApiService.getHistoricalData(
                ticker,
                interval.resolution,
                interval.startTime,
                interval.endTime
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

@OptIn(kotlin.time.ExperimentalTime::class)
sealed class StockDataInterval(val resolution: String, duration: Duration) {
    val endTime: Long
        get() {
            val calendar = Calendar.getInstance()
            return calendar.time.time.div(1000)
        }

    val startTime: Long = (endTime - duration.inSeconds).toLong()

    class Day : StockDataInterval("30", 1.days)

    class Week : StockDataInterval("D", 7.days)

    class Month : StockDataInterval("D", 30.days)

    class HalfYear : StockDataInterval("D", 180.days)

    class Year : StockDataInterval("D", 365.days)

    class All : StockDataInterval("W", Duration.INFINITE)
}