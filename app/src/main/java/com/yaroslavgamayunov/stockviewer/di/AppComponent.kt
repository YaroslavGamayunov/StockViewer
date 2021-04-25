package com.yaroslavgamayunov.stockviewer.di

import android.content.Context
import dagger.Component
import javax.inject.Named

@Component(modules = [AppModule::class])
@ApplicationScope
interface AppComponent {
    @Named("appContext")
    fun getContext(): Context
}