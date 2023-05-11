package com.example.finance

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Query

interface FinanceDataService {
    @GET("query/")
    fun getStockData(
        @Query("function") function: String,
        @Query("symbol") symbol:String,
        @Query("apikey") api_key:String
    ):Call<StockData>
}