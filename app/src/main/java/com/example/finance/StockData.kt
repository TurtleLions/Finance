package com.example.finance

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockData(
    @SerializedName("Meta Data")
    val metadata: Map<String, String>,
    @SerializedName("Time Series (Daily)")
    val dailyTimeSeries: Map<String, Map<String,String>>?,
    @SerializedName("Weekly Adjusted Time Series")
    val weeklyTimeSeries: Map<String, Map<String,String>>?,
    @SerializedName("Monthly Adjusted Time Series")
    val monthlyTimeSeries: Map<String, Map<String,String>>?
):Parcelable{
}
