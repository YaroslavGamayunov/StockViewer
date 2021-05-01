package com.yaroslavgamayunov.stockviewer.di

import android.content.Context
import androidx.room.Room
import com.yaroslavgamayunov.stockviewer.db.StockDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RoomModule {
    @Provides
    @RepositoryScope
    fun provideStockDataBase(@Named("appContext") context: Context): StockDatabase {
        return Room
            .databaseBuilder(context, StockDatabase::class.java, StockDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}