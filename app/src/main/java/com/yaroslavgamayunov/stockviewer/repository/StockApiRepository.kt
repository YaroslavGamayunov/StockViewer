package com.yaroslavgamayunov.stockviewer.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.vo.FavouriteStockItem
import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData
import com.yaroslavgamayunov.stockviewer.vo.NewsItem
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

// TODO: Create Separate repository for local operations(for methods where stock api is not needed)
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

    fun search(query: String): Flow<PagingData<StockItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = { db.stockItemDao().searchItems(query) }
        ).flow
    }

    suspend fun getStockItem(ticker: String): StockItem {
        return db.stockItemDao().getStockItem(ticker)
    }

    suspend fun popularTickers(n: Int): List<String> {
        return db.stockItemDao().popularTickers(n)
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

    suspend fun getHistoricalData(
        ticker: String,
        duration: StockDataDuration
    ): HistoricalCandleData? {
        return finHubApiService.getHistoricalData(
            ticker,
            duration.resoultion,
            duration.startTime,
            duration.endTime
        )
    }

    suspend fun getNews(ticker: String, startTime: Long, endTime: Long): List<NewsItem> {
        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(startTime))
        val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(endTime))
        return finHubApiService.getNews(ticker, startDate, endDate)
    }


    companion object {
        const val PAGE_SIZE = 20
    }
}

sealed class StockDataDuration {
    val endTime: Long
        get() {
            val calendar = Calendar.getInstance()
            return calendar.time.time.div(1000)
        }
    val startTime: Long
        get() {
            val calendar = Calendar.getInstance()
            when (this) {
                is All -> return 0
                is Day -> calendar.add(Calendar.DAY_OF_YEAR, -1)
                is HalfYear -> calendar.add(Calendar.MONTH, -6)
                is Month -> calendar.add(Calendar.MONTH, -1)
                is Week -> calendar.add(Calendar.WEEK_OF_YEAR, -1)
                is Year -> calendar.add(Calendar.YEAR, -1)
            }
            return calendar.time.time.div(1000)
        }

    val resoultion: String
        get() {
            return when (this) {
                is All -> "W"
                is Day -> "30"
                is HalfYear -> "D"
                is Month -> "D"
                is Week -> "60"
                is Year -> "D"
            }
        }

    object Day : StockDataDuration()

    object Week : StockDataDuration()

    object Month : StockDataDuration()

    object HalfYear : StockDataDuration()

    object Year : StockDataDuration()

    object All : StockDataDuration()
}