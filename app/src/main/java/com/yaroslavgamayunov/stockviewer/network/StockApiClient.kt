package com.yaroslavgamayunov.stockviewer.network

import android.util.Log
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlin.math.max
import kotlin.math.min

class StockApiClient(
    private val finHubApiService: FinHubApiService,
    private val iexCloudApiService: IexCloudApiService,
    private val stockIndex: String
) {

    private lateinit var tickers: List<String>
    val itemCount
        get() = tickers.size

    private suspend fun loadTickers(): List<String> {
        val constituents = finHubApiService.getConstituents(stockIndex)
        return constituents.tickers
    }

    suspend fun loadItems(page: Int, pageSize: Int): List<StockItem> {
        if (!::tickers.isInitialized) {
            tickers = loadTickers().sorted()
            Log.d("API", "tickers loaded: ${tickers.size} pieces")
        }

        val fromIndex = page * pageSize
        val toIndex = fromIndex + pageSize

        if (fromIndex > tickers.size || toIndex <= 0) {
            return listOf()
        }

        val queryTickers = tickers.subList(max(0, fromIndex), min(toIndex, tickers.size))
        val queryString = queryTickers.joinToString(",")

        Log.d("API", "queried ${queryTickers} tickers")

        return iexCloudApiService.getStockItems(queryString)
    }
}