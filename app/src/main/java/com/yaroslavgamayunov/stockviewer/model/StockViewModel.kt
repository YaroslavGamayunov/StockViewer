package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.repository.StockDataDuration
import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StockViewModel(private val repository: StockApiRepository) : ViewModel() {
    fun getStocksForIndexPaged(stockIndex: String): Flow<PagingData<StockItem>> {
        return repository.allItems(stockIndex).cachedIn(viewModelScope)
    }

    fun getFavouriteStocksPaged(): Flow<PagingData<StockItem>> {
        return repository.favouriteItems().cachedIn(viewModelScope)
    }

    suspend fun getStockItem(ticker: String): StockItem {
        return repository.getStockItem(ticker)
    }

    suspend fun getHistoricalCandleData(
        ticker: String,
        duration: StockDataDuration
    ): HistoricalCandleData? {
        return repository.getHistoricalData(ticker, duration)
    }


    fun favourite(ticker: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.favourite(ticker)
        }
    }

    fun unfavourite(ticker: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unfavourite(ticker)
        }
    }

    suspend fun isFavourite(ticker: String): Boolean {
        return repository.isFavourite(ticker)
    }
}