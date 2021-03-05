package com.yaroslavgamayunov.stockviewer.vo

import com.google.gson.annotations.SerializedName

data class IndexConstituentsResponse(
    @SerializedName("constituents")
    val tickers: List<String>,
    @SerializedName("symbol")
    val index: String
)