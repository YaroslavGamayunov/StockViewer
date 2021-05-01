package com.yaroslavgamayunov.stockviewer.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yaroslavgamayunov.stockviewer.BuildConfig
import com.yaroslavgamayunov.stockviewer.network.FinHubApiService
import com.yaroslavgamayunov.stockviewer.network.IexCloudApiService
import com.yaroslavgamayunov.stockviewer.vo.StockItem
import com.yaroslavgamayunov.stockviewer.vo.StockItemListResponseDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class NetworkServiceModule {

    @Provides
    @Named("finHubOkHttpClient")
    fun provideFinHubOkHttpClient(): OkHttpClient {
        return createOkHttpClient(apiToken = BuildConfig.FINHUB_API_TOKEN)
    }

    @Provides
    @Named("iexCloudOkHttpClient")
    fun provideIexCloudOkHttpClient(): OkHttpClient {
        return createOkHttpClient(apiToken = BuildConfig.IEX_API_TOKEN)
    }

    @Provides
    @RepositoryScope
    fun provideIexCloudApiService(
        @Named("iexCloudOkHttpClient") client: OkHttpClient,
        @Named("iexCloudServiceGson") gson: Gson
    ): IexCloudApiService {
        return createService(
            IexCloudApiService.BASE_URL,
            gson,
            client,
            IexCloudApiService::class.java
        )
    }

    @Provides
    @RepositoryScope
    fun provideFinHubApiService(
        @Named("finHubOkHttpClient") client: OkHttpClient,
        @Named("finHubServiceGson") gson: Gson
    ): FinHubApiService {
        return createService(
            FinHubApiService.BASE_URL,
            gson,
            client,
            FinHubApiService::class.java
        )
    }

    @Provides
    @Named("finHubServiceGson")
    fun provideFinHubServiceGson(): Gson {
        return Gson()
    }

    @Provides
    @Named("iexCloudServiceGson")
    fun provideIexCloudServiceGson(): Gson {
        return GsonBuilder().registerTypeAdapter(
            TypeToken.getParameterized(List::class.java, StockItem::class.java).type,
            StockItemListResponseDeserializer()
        ).create()
    }

    private fun <T> createService(
        baseUrl: String,
        gson: Gson,
        client: OkHttpClient,
        serviceClass: Class<T>
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(serviceClass)
    }

    private fun createOkHttpClient(apiToken: String): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor { chain ->
            var request = chain.request()
            val url = request
                .url
                .newBuilder()
                .addQueryParameter("token", apiToken)
                .build()
            request = request.newBuilder().url(url).build()
            return@addInterceptor chain.proceed(request)
        }.addInterceptor(logging).build()
    }
}