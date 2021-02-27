package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yaroslavgamayunov.stockviewer.data.StockApiRepository
import com.yaroslavgamayunov.stockviewer.data.StockItem
import kotlinx.coroutines.flow.Flow

class StockViewModel(private val repository: StockApiRepository) : ViewModel() {
    fun getStocksForIndex(stockIndex: String): Flow<PagingData<StockItem>> {
        return repository.getStockItemFlow(stockIndex).cachedIn(viewModelScope)
    }
}