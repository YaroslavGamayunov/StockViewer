package com.yaroslavgamayunov.stockviewer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yaroslavgamayunov.stockviewer.vo.FavouriteStockItem
import com.yaroslavgamayunov.stockviewer.vo.RemoteKeys
import com.yaroslavgamayunov.stockviewer.vo.StockItem

@Database(
    entities = [StockItem::class, FavouriteStockItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract fun favouritesDao(): FavouriteStockItemsDao
    abstract fun stockItemDao(): StockItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DATABASE_NAME = "stock_items.db"
    }
}