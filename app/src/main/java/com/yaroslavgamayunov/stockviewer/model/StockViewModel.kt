package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlinx.coroutines.flow.Flow

class StockViewModel(private val repository: StockApiRepository) : ViewModel() {
    fun getStocksForIndex(stockIndex: String): Flow<PagingData<StockItem>> {
        return repository.allItems(stockIndex).cachedIn(viewModelScope)
    }
}