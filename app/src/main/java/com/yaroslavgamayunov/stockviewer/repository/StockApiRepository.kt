package com.yaroslavgamayunov.stockviewer.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.vo.FavouriteStockItem
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlinx.coroutines.flow.Flow

class StockApiRepository(
    private val iexCloudApiService: IexCloudApiService,
    private val finHubApiService: FinHubApiService,
    private val db: StockDatabase
) {
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

    fun favouriteItems(): Flow<PagingData<StockItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = { db.stockItemDao().getFavourites() }
        ).flow
    }

    suspend fun favourite(ticker: String) {
        db.withTransaction {
            db.favouritesDao().favourite(FavouriteStockItem(ticker))
            db.stockItemDao().favourite(listOf(ticker))
        }
    }

    suspend fun unfavourite(ticker: String) {
        db.withTransaction {
            db.favouritesDao().unfavourite(FavouriteStockItem(ticker))
            db.stockItemDao().unfavourite(listOf(ticker))
        }
    }

    suspend fun isFavourite(ticker: String): Boolean {
        return db.favouritesDao().isFavourite(ticker) == 1
    }


    companion object {
        const val PAGE_SIZE = 20
    }
}