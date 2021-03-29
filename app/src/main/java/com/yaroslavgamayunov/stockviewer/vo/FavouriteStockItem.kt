package com.yaroslavgamayunov.stockviewer.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This table is made to be able to persist user's favourite items, because api has no support
 * of favourite stock items
 */
@Entity(tableName = "favourite_stock_items")
data class FavouriteStockItem(@PrimaryKey val ticker: String)