package com.yaroslavgamayunov.stockviewer.db


import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaroslavgamayunov.stockviewer.vo.StockItem

@Dao
interface StockItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<StockItem>): List<Long>

    @Query("SELECT * FROM stock_items ORDER BY ticker ASC")
    fun getAll(): PagingSource<Int, StockItem>

    @Query("SELECT * FROM stock_items WHERE isFavourite=1 ORDER BY ticker ASC")
    fun getFavourites(): PagingSource<Int, StockItem>

    @Query("UPDATE stock_items SET isFavourite=1 WHERE ticker IN (:tickers)")
    suspend fun favourite(tickers: List<String>)

    @Query("UPDATE stock_items SET isFavourite=0 WHERE ticker IN (:tickers)")
    suspend fun unfavourite(tickers: List<String>)

    @Query("DELETE FROM stock_items")
    suspend fun clearStockItems(): Int

    @Query("SELECT * FROM stock_items WHERE ticker LIKE :query OR name LIKE :query ORDER BY ticker ASC")
    fun searchItems(query: String): PagingSource<Int, StockItem>

    @Query("SELECT ticker FROM stock_items ORDER BY (latestPrice - previousDayClosePrice) DESC LIMIT :n")
    suspend fun popularTickers(n: Int): List<String>
}