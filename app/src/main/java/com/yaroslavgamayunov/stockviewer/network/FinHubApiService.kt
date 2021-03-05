package com.yaroslavgamayunov.stockviewer.network

import com.yaroslavgamayunov.stockviewer.BuildConfig
import com.yaroslavgamayunov.stockviewer.vo.IndexConstituentsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FinHubApiService {
    @GET("index/constituents?")
    suspend fun getConstituents(
        @Query("symbol") stockIndex: String,
    ): IndexConstituentsResponse

    companion object Factory {
        private const val BASE_URL = "https://finnhub.io/api/v1/"

        fun create(): FinHubApiService {
            val client = OkHttpClient.Builder().addInterceptor { chain ->
                var request = chain.request()
                val url = request
                    .url()
                    .newBuilder()
                    .addQueryParameter("token", BuildConfig.FINHUB_API_TOKEN)
                    .build()
                request = request.newBuilder().url(url).build()
                return@addInterceptor chain.proceed(request)
            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(FinHubApiService::class.java)
        }
    }
}