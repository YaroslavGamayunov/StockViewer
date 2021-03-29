package com.yaroslavgamayunov.stockviewer.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val ticker: String,
    val currentKey: Int?,
    val prevKey: Int?,
    val nextKey: Int?
)