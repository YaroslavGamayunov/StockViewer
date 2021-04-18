package com.yaroslavgamayunov.stockviewer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class StockViewerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //TODO: Add support of dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupAppMetrica()
    }

    private fun setupAppMetrica() {

        // Creating an extended library configuration.
        val config: YandexMetricaConfig =
            YandexMetricaConfig.newConfigBuilder(BuildConfig.YANDEX_METRICA_API_TOKEN)
                .withLogs()
                .build()

        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)

        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this)
    }
}