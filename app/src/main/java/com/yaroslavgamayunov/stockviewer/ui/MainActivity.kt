package com.yaroslavgamayunov.stockviewer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yaroslavgamayunov.stockviewer.BuildConfig
import com.yaroslavgamayunov.stockviewer.R
import com.yaroslavgamayunov.stockviewer.network.StockApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        GlobalScope.launch(Dispatchers.IO) {
//            val tickers =
//                StockApiClient.webService.getConstituents(
//                    "^NDX",
//                    BuildConfig.FINHUB_API_KEY
//                ).constituents
//            tickers.take(30).forEach { ticker ->
//                val company =
//                    StockApiClient.webService.getCompany(
//                        ticker,
//                        BuildConfig.FINHUB_API_KEY
//                    )
//                Log.d("akakakaka", company.toString())
//            }
//        }
    }
}