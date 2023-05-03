package com.example.finance

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockData(
    @SerializedName("Meta Data")
    val metadata:MetaData,
    @SerializedName("Time Series (Daily)")
    val dailyTimeSeries: Map<String, InterStockData>?,
    @SerializedName("Weekly Adjusted Time Series")
    val weeklyTimeSeries: Map<String, InterStockData>?,
    @SerializedName("Monthly Adjusted Time Series")
    val monthlyTimeSeries: Map<String, InterStockData>?
):Parcelable{
    @Parcelize
    data class MetaData(
        @SerializedName("1. Information")
        val information: String,
        @SerializedName("2. Symbol")
        val symbol: String,
        @SerializedName("3. Last Refreshed")
        val lastRefreshed: String,
        @SerializedName("4. Interval")
        val interval: String,
        @SerializedName("5. Output Size")
        val outputSize: String,
        @SerializedName("6. Time Zone")
        val timeZone: String
    ):Parcelable
}
