package com.yaroslavgamayunov.stockviewer.di

import android.app.Application
import android.content.Context
import com.yaroslavgamayunov.stockviewer.StockViewerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule(val application: StockViewerApplication) {

    @Named("appContext")
    @Provides
    @ApplicationScope
    fun provideAppContext(@Named("application") application: Application): Context {
        return application.applicationContext
    }

    @Named("application")
    @Provides
    @ApplicationScope
    fun provideApplication(): Application {
        return application
    }
}