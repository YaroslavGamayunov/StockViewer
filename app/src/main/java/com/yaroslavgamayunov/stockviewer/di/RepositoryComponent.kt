package com.yaroslavgamayunov.stockviewer.di

import com.yaroslavgamayunov.stockviewer.ui.*
import dagger.Subcomponent

@Subcomponent(modules = [NetworkServiceModule::class, RoomModule::class])
@RepositoryScope
interface RepositoryComponent {
    fun inject(companyInfoFragment: CompanyInfoFragment)
    fun inject(companyNewsFragment: CompanyNewsFragment)
    fun inject(stockChartFragment: StockChartFragment)
    fun inject(stockDetailFragment: StockDetailFragment)
    fun inject(stockListFragment: StockListFragment)
    fun inject(stockSearchFragment: StockSearchFragment)

    @Subcomponent.Builder
    interface Builder {
        fun networkServiceModule(module: NetworkServiceModule): Builder
        fun roomModule(module: RoomModule): Builder
        fun build(): RepositoryComponent
    }
}