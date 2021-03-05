package com.yaroslavgamayunov.stockviewer.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlinx.coroutines.flow.Flow

class StockApiRepository(
    private val iexCloudApiService: IexCloudApiService,
    private val finHubApiService: FinHubApiService,
    private val db: StockDatabase
) {
    @ExperimentalPagingApi
    fun allItems(stockIndex: String): Flow<PagingData<StockItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
            ),
            remoteMediator = StockPageKeyedRemoteMediator(
                db,
                iexCloudApiService,
                finHubApiService,
                stockIndex
            ),
            pagingSourceFactory = { db.stockItemDao().getAll() }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}