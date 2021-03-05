package com.yaroslavgamayunov.stockviewer.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.vo.RemoteKeys
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.ceil

@OptIn(ExperimentalPagingApi::class)
class StockPageKeyedRemoteMediator(
    private val db: StockDatabase,
    private val iexCloudApiService: IexCloudApiService,
    private val finHubApiService: FinHubApiService,
    private val stockIndex: String
) : RemoteMediator<Int, StockItem>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StockItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.currentKey ?: 0
            }
            LoadType.PREPEND -> {
                val firstItem = state.firstItemOrNull() ?: return MediatorResult.Success(true)

                val remoteKeys = db.remoteKeysDao().remoteKeysByTicker(firstItem.ticker)
                remoteKeys?.prevKey ?: return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(true)

                val remoteKeys = db.remoteKeysDao().remoteKeysByTicker(lastItem.ticker)
                remoteKeys?.nextKey ?: return MediatorResult.Success(true)
            }
        }

        try {
            val endOfPaginationReached = db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.stockItemDao().clearStockItems()
                }

                val keys = loadRemoteKeys(state.config.pageSize)
                db.remoteKeysDao().insertAll(keys)

                val stockItems = loadPage(page)
                db.stockItemDao().insertAll(stockItems)

                return@withTransaction stockItems.isEmpty()
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, StockItem>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.ticker?.let { repoId ->
                db.remoteKeysDao().remoteKeysByTicker(repoId)
            }
        }
    }

    private suspend fun loadRemoteKeys(pageSize: Int): List<RemoteKeys> {
        val tickers = loadTickers(stockIndex)

        val countOfPages = ceil(tickers.size.toDouble() / pageSize)

        return tickers.mapIndexed { index, ticker ->
            val currentKey = index / pageSize
            val nextKey =
                if (currentKey + 1 < countOfPages) currentKey + 1 else null
            val prevKey = if (currentKey > 0) currentKey - 1 else null
            RemoteKeys(ticker, currentKey, prevKey, nextKey)
        }
    }

    private suspend fun loadTickers(stockIndex: String): List<String> =
        finHubApiService.getConstituents(stockIndex).tickers.sorted()

    private suspend fun loadPage(page: Int): List<StockItem> {
        val tickers = db.remoteKeysDao().remoteKeysByPage(page).map { it.ticker }

        val query = tickers.joinToString(",")
        return iexCloudApiService.getStockItems(query)
    }
}