package com.yaroslavgamayunov.stockviewer.db

import androidx.room.*
import com.yaroslavgamayunov.stockviewer.vo.FavouriteStockItem

@Dao
interface FavouriteStockItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favourite(item: FavouriteStockItem)

    @Delete
    suspend fun unfavourite(item: FavouriteStockItem)

    @Query("SELECT EXISTS (SELECT 1 FROM favourite_stock_items WHERE ticker=:ticker)")
    suspend fun isFavourite(ticker: String): Int

    @Query("SELECT * FROM favourite_stock_items WHERE ticker IN (:tickers)")
    suspend fun filterFavourites(tickers: List<String>): List<FavouriteStockItem>

    @Query("DELETE FROM favourite_stock_items")
    suspend fun clearFavourites()
}