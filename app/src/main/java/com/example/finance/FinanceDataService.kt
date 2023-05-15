package com.example.finance

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.Url

interface FinanceDataService {
    @GET
    fun getDailyStockData(
        @Query("function") function: String,
        @Query("symbol") symbol:String,
        @Query("apikey") api_key:String
    ):Call<StockData>
    @GET
    fun getWeeklyStockData(
        @Query("function") function: String,
        @Query("symbol") symbol:String,
        @Query("apikey") api_key:String
    ):Call<StockData>
    @GET
    fun getMonthlyStockData(
        @Query("function") function: String,
        @Query("symbol") symbol:String,
        @Query("apikey") api_key:String
    ):Call<StockData>
}