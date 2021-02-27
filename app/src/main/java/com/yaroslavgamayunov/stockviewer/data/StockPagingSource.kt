package com.yaroslavgamayunov.stockviewer.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yaroslavgamayunov.stockviewer.network.StockApiClient
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService

class StockPagingSource(
    private val client: StockApiClient,
) : PagingSource<Int, StockItem>() {

    private fun loadInitial(): LoadResult<Int, StockItem> {
        return try {
            LoadResult.Page(
                listOf(),
                prevKey = null,
                nextKey = 0,
                itemsAfter = StockApiRepository.PAGE_SIZE
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StockItem> {

        val position = params.key ?: return loadInitial()

        val startIndex = position * StockApiRepository.PAGE_SIZE
        val endIndex = startIndex + params.loadSize

        val nextKey = position + params.loadSize / StockApiRepository.PAGE_SIZE
        return try {
            val items = client.loadItems(startIndex, endIndex)
            Log.d("llllllll", "loaded ${items.size} items in range ${startIndex}:${endIndex}")
            LoadResult.Page(
                items,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (items.isEmpty()) null else nextKey,
                itemsAfter = client.itemCount - position * StockApiRepository.PAGE_SIZE
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StockItem>): Int? {
        return state.anchorPosition
    }

}