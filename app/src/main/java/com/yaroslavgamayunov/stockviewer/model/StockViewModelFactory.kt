package com.yaroslavgamayunov.stockviewer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.repository.StockApiRepository
import com.yaroslavgamayunov.stockviewer.repository.StockDatabaseRepository

class StockViewModelFactory(
    iexCloudApiService: IexCloudApiService,
    finHubApiService: FinHubApiService,
    db: StockDatabase
) : ViewModelProvider.Factory {
    private val stockDatabaseRepository =
        StockDatabaseRepository(iexCloudApiService, finHubApiService, db)

    private val stockApiRepository =
        StockApiRepository(iexCloudApiService, finHubApiService)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockDatabaseViewModel::class.java)) {
            return StockDatabaseViewModel(stockDatabaseRepository) as T
        }
        if (modelClass.isAssignableFrom(StockSearchViewModel::class.java)) {
            return StockSearchViewModel(stockDatabaseRepository) as T
        }

        if (modelClass.isAssignableFrom(StockApiViewModel::class.java)) {
            return StockApiViewModel(stockApiRepository) as T
        }
        throw IllegalArgumentException("Unsupported ViewModel class")
    }

}