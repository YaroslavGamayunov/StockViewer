package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yaroslavgamayunov.stockviewer.repository.StockDatabaseRepository
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StockDatabaseViewModel(private val repository: StockDatabaseRepository) : ViewModel() {
    fun getStocksForIndexPaged(stockIndex: String): Flow<PagingData<StockItem>> {
        return repository.allItems(stockIndex).cachedIn(viewModelScope)
    }

    fun getFavouriteStocksPaged(): Flow<PagingData<StockItem>> {
        return repository.favouriteItems().cachedIn(viewModelScope)
    }

    suspend fun getStockItem(ticker: String): StockItem {
        return repository.getStockItem(ticker)
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