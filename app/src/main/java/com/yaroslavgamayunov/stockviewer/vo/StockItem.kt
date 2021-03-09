package com.yaroslavgamayunov.stockviewer.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

@Entity(tableName = "stock_items")
data class StockItem(
    @PrimaryKey
    @SerializedName("symbol")
    val ticker: String,
    @SerializedName("companyName")
    val name: String,
    @SerializedName("previousClose")
    val previousDayClosePrice: Double,
    val currentPrice: Double?,
    val isFavourite: Boolean = false,
    val logoUrl: String
)

// This deserializer is used only when processing Iex api responses
class StockItemListResponseDeserializer : JsonDeserializer<List<StockItem>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<StockItem> {
        if (json == null) {
            return listOf()
        }

        val gson = Gson()
        val entries = json.asJsonObject.entrySet()

        return entries.map { (_, companyJson) ->
            val companyObject = companyJson.asJsonObject.deepCopy()
            val logoUrl = companyObject["logo"].asJsonObject["url"].asString
            companyObject["quote"].asJsonObject.add("logoUrl", JsonPrimitive(logoUrl))

            gson.fromJson(companyObject["quote"], StockItem::class.java)
        }
    }
}