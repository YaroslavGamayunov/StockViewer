package com.yaroslavgamayunov.stockviewer.data

import com.google.gson.annotations.SerializedName

data class IndexConstituents(
    @SerializedName("constituents")
    val tickers: List<String>,
    @SerializedName("symbol")
    val index: String
)