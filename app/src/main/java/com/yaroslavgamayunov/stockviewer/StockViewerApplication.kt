package com.yaroslavgamayunov.stockviewer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.yaroslavgamayunov.stockviewer.di.AppComponent
import com.yaroslavgamayunov.stockviewer.di.AppModule
import com.yaroslavgamayunov.stockviewer.di.DaggerAppComponent

class StockViewerApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        //TODO: Add support of dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setupAppComponent()
        setupAppMetrica()
    }

    private fun setupAppComponent() {
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
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