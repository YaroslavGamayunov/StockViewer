package com.yaroslavgamayunov.stockviewer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class StockViewerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //TODO: Add support of dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}