package com.yaroslavgamayunov.stockviewer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yaroslavgamayunov.stockviewer.vo.RemoteKeys
import com.yaroslavgamayunov.stockviewer.vo.StockItem

@Database(
    entities = [StockItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "stock_items.db"

        @Volatile
        private var INSTANCE: StockDatabase? = null

        fun getInstance(context: Context): StockDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: create(context).also { INSTANCE = it }
            }

        private fun create(context: Context): StockDatabase {
            return Room
                .databaseBuilder(context, StockDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun stockItemDao(): StockItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}