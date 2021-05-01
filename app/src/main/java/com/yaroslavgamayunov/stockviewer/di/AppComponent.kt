package com.yaroslavgamayunov.stockviewer.di

import dagger.Component

@Component(modules = [AppModule::class])
@ApplicationScope
interface AppComponent {
    fun repositoryComponentBuilder(): RepositoryComponent.Builder
}