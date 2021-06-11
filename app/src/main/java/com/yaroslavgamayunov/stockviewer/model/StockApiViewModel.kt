package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.repository.StockDataDuration
import com.yaroslavgamayunov.stockviewer.utils.CallResult
import com.yaroslavgamayunov.stockviewer.vo.CompanyInfo
import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData
import com.yaroslavgamayunov.stockviewer.vo.NewsItem

class StockApiViewModel(private val repository: StockApiRepository) : ViewModel() {
    suspend fun getNews(
        ticker: String,
        startTime: Long,
        endTime: Long
    ): CallResult<List<NewsItem>> {
        return repository.getNews(ticker, startTime, endTime)
    }

    suspend fun getHistoricalCandleData(
        ticker: String,
        duration: StockDataDuration
    ): CallResult<HistoricalCandleData> {
        return repository.getHistoricalData(ticker, duration)
    }

    suspend fun getCompanyInfo(ticker: String): CallResult<CompanyInfo> {
        return repository.getCompanyInfo(ticker)
    }
}