package com.yaroslavgamayunov.stockviewer.vo

import com.google.gson.annotations.SerializedName


data class NewsItem(
    @SerializedName("datetime")
    val date: Long,
    val headline: String,
    @SerializedName("image")
    val imageUrl: String,
    val source: String,
    val summary: String,
    @SerializedName("url")
    val newsUrl: String,
    val id: Int
)