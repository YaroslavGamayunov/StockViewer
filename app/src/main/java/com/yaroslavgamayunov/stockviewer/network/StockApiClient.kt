package com.yaroslavgamayunov.stockviewer.network

import android.util.Log
import com.yaroslavgamayunov.stockviewer.data.StockItem
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

    suspend fun loadItems(fromIndex: Int, toIndex: Int): List<StockItem> {
        if (!::tickers.isInitialized) {
            tickers = loadTickers().sorted()
            Log.d("llllllll", "tickers loaded: ${tickers.size} pieces")
        }

        val queryTickers = tickers.subList(fromIndex, min(toIndex, tickers.size))
        val queryString = queryTickers.joinToString(",")

        Log.d("llllllll", "queried ${queryTickers} tickers")

        return iexCloudApiService.getCompanies(queryString).map { StockItem(it) }
    }
}