package com.yaroslavgamayunov.stockviewer.di

import android.net.Uri
import dagger.Component
import javax.inject.Named

@Component(
    dependencies = [AppComponent::class],
    modules = [SettingsModule::class]
)
@SettingsScreenScope
interface SettingsComponent {
    @Named("appInfoPageUri")
    fun getAppInfoPageUri(): Uri
}