package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository

class StockViewModelFactory(private val stockApiRepository: StockApiRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            return StockViewModel(stockApiRepository) as T
        }
        throw IllegalArgumentException("Unsupported ViewModel class")
    }

}