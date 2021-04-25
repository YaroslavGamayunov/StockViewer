package com.yaroslavgamayunov.stockviewer.network

import com.yaroslavgamayunov.stockviewer.BuildConfig
import com.yaroslavgamayunov.stockviewer.vo.HistoricalCandleData
import com.yaroslavgamayunov.stockviewer.vo.IndexConstituentsResponse
import com.yaroslavgamayunov.stockviewer.vo.NewsItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FinHubApiService {
    @GET("index/constituents?")
    suspend fun getConstituents(
        @Query("symbol") stockIndex: String,
    ): IndexConstituentsResponse

    @GET("stock/candle")
    suspend fun getHistoricalData(
        @Query("symbol") ticker: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long
    ): HistoricalCandleData?

    @GET("company-news")
    suspend fun getNews(
        @Query("symbol") ticker: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): List<NewsItem>

    companion object Factory {
        private const val BASE_URL = "https://finnhub.io/api/v1/"

        fun create(): FinHubApiService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor { chain ->
                var request = chain.request()
                val url = request
                    .url
                    .newBuilder()
                    .addQueryParameter("token", BuildConfig.FINHUB_API_TOKEN)
                    .build()
                request = request.newBuilder().url(url).build()
                return@addInterceptor chain.proceed(request)
            }.addInterceptor(logging).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(FinHubApiService::class.java)
        }
    }
}