package com.yaroslavgamayunov.stockviewer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yaroslavgamayunov.stockviewer.vo.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(constituents: List<RemoteKeys>)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys(): Int

    @Query("SELECT * FROM remote_keys WHERE ticker=:ticker")
    suspend fun remoteKeysByTicker(ticker: String): RemoteKeys?

    @Query("SELECT * FROM remote_keys WHERE currentKey=:page")
    suspend fun remoteKeysByPage(page: Int): List<RemoteKeys>
}