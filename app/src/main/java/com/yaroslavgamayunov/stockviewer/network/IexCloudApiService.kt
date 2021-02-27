package com.yaroslavgamayunov.stockviewer.network

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yaroslavgamayunov.stockviewer.BuildConfig
import com.yaroslavgamayunov.stockviewer.data.Company
import com.yaroslavgamayunov.stockviewer.data.CompanyListResponseDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface IexCloudApiService {
    @GET("stock/market/batch?types=company,logo")
    suspend fun getCompanies(
        @Query("symbols", encoded = true) symbols: String,
    ): List<Company>

    companion object Factory {
        private const val BASE_URL = "https://cloud.iexapis.com/v1/"

        fun create(): IexCloudApiService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY


            val client = OkHttpClient.Builder().addInterceptor { chain ->
                var request = chain.request()
                val url = request
                    .url()
                    .newBuilder()
                    .addQueryParameter("token", BuildConfig.IEX_API_TOKEN)
                    .build()
                request = request.newBuilder().url(url).build()
                return@addInterceptor chain.proceed(request)
            }.addInterceptor(logging).build()

            val gson = GsonBuilder().registerTypeAdapter(
                TypeToken.getParameterized(List::class.java, Company::class.java).type,
                CompanyListResponseDeserializer()
            ).create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(IexCloudApiService::class.java)
        }
    }
}