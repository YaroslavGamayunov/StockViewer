package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaroslavgamayunov.stockviewer.data.StockApiRepository
import java.lang.IllegalArgumentException

class StockViewModelFactory(private val stockApiRepository: StockApiRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            return StockViewModel(stockApiRepository) as T
        }
        throw IllegalArgumentException("Unsupported ViewModel class")
    }

}