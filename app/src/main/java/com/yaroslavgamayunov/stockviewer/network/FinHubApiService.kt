package com.yaroslavgamayunov.stockviewer.network

import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData
import com.yaroslavgamayunov.stockviewer.vo.IndexConstituentsResponse
import com.yaroslavgamayunov.stockviewer.vo.NewsItem
import retrofit2.http.GET
import retrofit2.http.Query

interface FinHubApiService {
    @GET("index/constituents?")
    suspend fun getConstituents(
        @Query("symbol") stockIndex: String,
    ): IndexConstituentsResponse

    @GET("stock/candle")
    suspend fun getHistoricalData(
        @Query("symbol") ticker: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): HistoricalCandleData?

    @GET("company-news")
    suspend fun getNews(
        @Query("symbol") ticker: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): List<NewsItem>

    companion object {
        const val BASE_URL = "https://finnhub.io/api/v1/"
    }
}