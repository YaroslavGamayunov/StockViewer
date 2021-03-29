package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository

class StockViewModelFactory(private val stockApiRepository: StockApiRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockDatabaseViewModel::class.java)) {
            return StockDatabaseViewModel(stockApiRepository) as T
        }
        if (modelClass.isAssignableFrom(StockSearchViewModel::class.java)) {
            return StockSearchViewModel(stockApiRepository) as T
        }

        if (modelClass.isAssignableFrom(StockApiViewModel::class.java)) {
            return StockApiViewModel(stockApiRepository) as T
        }
        throw IllegalArgumentException("Unsupported ViewModel class")
    }

}