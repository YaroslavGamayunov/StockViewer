package com.yaroslavgamayunov.stockviewer.vo

import com.google.gson.annotations.SerializedName

data class HistoricalCandleData(
    @SerializedName("o")
    val openPrices: List<Double>,
    @SerializedName("h")
    val highPrices: List<Double>,
    @SerializedName("l")
    val lowPrices: List<Double>,
    @SerializedName("c")
    val closePrices: List<Double>,
    @SerializedName("t")
    val timeStamps: List<Long>
)
