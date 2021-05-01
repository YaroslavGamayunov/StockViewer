package com.yaroslavgamayunov.stockviewer.di

import android.net.Uri
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SettingsModule {
    @Provides
    @Named("appInfoPageUri")
    fun provideAppInfoPageUri(): Uri {
        return Uri.parse("https://yaroslavgamayunov.github.io/StockViewer/")
    }
}