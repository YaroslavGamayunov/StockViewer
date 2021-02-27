package com.yaroslavgamayunov.stockviewer.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.network.StockApiClient
import kotlinx.coroutines.flow.Flow

class StockApiRepository(
    private val iexCloudApiService: IexCloudApiService,
    private val finHubApiService: FinHubApiService
) {

    fun getStockItemFlow(stockIndex: String): Flow<PagingData<StockItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                StockPagingSource(StockApiClient(finHubApiService, iexCloudApiService, stockIndex))
            }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}