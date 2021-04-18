package com.yaroslavgamayunov.stockviewer.vo

import com.google.gson.annotations.SerializedName

data class CompanyInfo(
    val symbol: String,
    val companyName: String,
    val exchange: String,
    val industry: String,
    val website: String,
    val description: String,

    @SerializedName("CEO")
    val ceo: String,

    val securityName: String,
    val issueType: String,
    val sector: String,
    val primarySicCode: String?,
    val employees: Long,
    val tags: List<String>,
    val address: String?,
    val state: String?,
    val city: String?,
    val zip: String?,
    val country: String?,
    val phone: String?
) {
    fun getAddressString(): String {
        if (address == null || city == null || country == null) {
            return ""
        }
        return "$address, $city, $country"
    }
}