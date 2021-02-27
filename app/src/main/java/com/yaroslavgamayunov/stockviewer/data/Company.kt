package com.yaroslavgamayunov.stockviewer.data

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class Company(
    @SerializedName("symbol")
    val ticker: String,
    @SerializedName("companyName")
    val name: String,
    @SerializedName("marketCap")
    val marketCapitalization: Double,
    val logoUrl: String
)

// This deserializer is used only when processing Iex api responses
class CompanyListResponseDeserializer : JsonDeserializer<List<Company>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Company> {
        if (json == null) {
            return listOf()
        }

        val gson = Gson()
        val entries = json.asJsonObject.entrySet()

        return entries.map { (_, companyJson) ->
            val companyObject = companyJson.asJsonObject.deepCopy()
            val logoUrl = companyObject["logo"].asJsonObject["url"].asString
            companyObject["company"].asJsonObject.add("logoUrl", JsonPrimitive(logoUrl))

            gson.fromJson(companyObject["company"], Company::class.java)
        }
    }

}