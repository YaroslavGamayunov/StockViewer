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

    @Query("UPDATE stock_items SET isFavourite=:isFavourite WHERE ticker=:ticker")
    suspend fun setFavourite(ticker: String, isFavourite: Boolean)

    @Query("DELETE FROM stock_items")
    suspend fun clearStockItems(): Int
}