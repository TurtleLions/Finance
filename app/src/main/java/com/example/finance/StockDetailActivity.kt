package com.example.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.finance.databinding.ActivityStockDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockDetailBinding
    private lateinit var stock: Stock
    private lateinit var stockData:StockData
    companion object{
        val TAG = "Stock Detail Activity"
        val EXTRA_CURRENTSTOCK = "current stock"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stock = intent.getParcelableExtra<Stock>(SearchAdapter.EXTRA_CURRENTSTOCK)!!
        binding.detailName.text = stock.name
        binding.detailSymbol.text = stock.ticker
        binding.detailExchange.text = stock.exchange

    }
    suspend fun getStockDataByApiCall(function:String, symbol:String) {
        val FinanceDataService = RetrofitHelper.getInstance().create(FinanceDataService::class.java)
        val stockDataCall = FinanceDataService.getStockData(function,
            symbol, Constants.API_KEY)
        stockDataCall.enqueue(object: Callback<StockData> {
            override fun onResponse(
                call: Call<StockData>,
                response: Response<StockData>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
                stockData = response.body()!!
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
}