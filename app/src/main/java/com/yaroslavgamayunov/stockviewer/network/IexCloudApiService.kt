package com.yaroslavgamayunov.stockviewer.network

import com.yaroslavgamayunov.stockviewer.vo.CompanyInfo
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface IexCloudApiService {
    @GET("stock/market/batch?types=quote,logo")
    suspend fun getStockItems(
        @Query("symbols", encoded = true) tickers: String,
    ): List<StockItem>

    @GET("/stable/stock/{symbol}/company")
    suspend fun getCompanyInfo(@Path("symbol") ticker: String): CompanyInfo

    companion object Factory {
        const val BASE_URL = "https://cloud.iexapis.com/v1/"
    }
}