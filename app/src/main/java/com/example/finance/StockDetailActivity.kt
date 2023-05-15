package com.example.finance

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.finance.databinding.ActivityStockDetailBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

//import com.jjoe64.graphview.GraphView
//import com.jjoe64.graphview.series.DataPoint
//import com.jjoe64.graphview.series.LineGraphSeries

class StockDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockDetailBinding
    private lateinit var stock: Stock
    private lateinit var dailyStockData:StockData
    private lateinit var weeklyStockData:StockData
    private lateinit var monthlyStockData:StockData
    private lateinit var lineGraphView: GraphView
    private var timeState = 0
    companion object{
        val TAG = "Stock Detail Activity"
        val EXTRA_CURRENTSTOCK = "current stock"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val stock = intent.getParcelableExtra<Stock>(SearchAdapter.EXTRA_CURRENTSTOCK)!!
        binding.detailName.text = stock.name
        binding.detailSymbol.text = stock.ticker
        binding.detailExchange.text = stock.exchange
        binding.buttonTimeGraph.text = "Daily"
//        lineGraphView = findViewById(R.id.idGraphView)
        binding.buttonTimeGraph.setOnClickListener {
            if(timeState==0){
                binding.buttonTimeGraph.text = "Weekly"
                timeState=1
            }
            else if(timeState==1){
                binding.buttonTimeGraph.text = "Monthly"
                timeState=2
            }
            else if(timeState==2){
                binding.buttonTimeGraph.text = "Daily"
                timeState=0
            }

        }
        GlobalScope.launch {
            async {
                getDailyStockDataByApiCall(Constants.DAILY, binding.detailSymbol.text.toString())
                getWeeklyStockDataByApiCall(Constants.WEEKLY, binding.detailSymbol.text.toString())
                getMonthStockDataByApiCall(Constants.MONTHLY, binding.detailSymbol.text.toString())
            }
            while (!this@StockDetailActivity::dailyStockData.isInitialized || !this@StockDetailActivity::weeklyStockData.isInitialized || !this@StockDetailActivity::monthlyStockData.isInitialized) {

            }
            Log.d(TAG, dailyStockData.toString())
            Log.d(TAG, weeklyStockData.toString())
            Log.d(TAG, monthlyStockData.toString())


            lineGraphView = findViewById(R.id.idGraphView)
            val now = Instant.now()
            val yesterday = Instant.now().minus(1, ChronoUnit.DAYS)
            Log.d(TAG, now.toString())
            Log.d(TAG, yesterday.toString())


            val series: LineGraphSeries<DataPoint> = LineGraphSeries(
                arrayOf(
                    // on below line we are adding
                    // each point on our x and y axis.
                    //dailyStockData.dailyTimeSeries?.get("$")?.get("2. high")
                    //?.let { DataPoint(0.0, it.toDouble()) },
                    DataPoint(1.0, 3.0),
                    DataPoint(2.0, 4.0),
                    DataPoint(3.0, 9.0),
                    DataPoint(4.0, 6.0),
                    DataPoint(5.0, 3.0),
                    DataPoint(6.0, 6.0),
                    DataPoint(7.0, 1.0),
                    DataPoint(8.0, 2.0)
                )
            )

            // on below line adding animation
            lineGraphView.animate()

            // on below line we are setting scrollable
            // for point graph view
            lineGraphView.viewport.isScrollable = true

            // on below line we are setting scalable.
            lineGraphView.viewport.isScalable = true

            // on below line we are setting scalable y
            lineGraphView.viewport.setScalableY(true)

            // on below line we are setting scrollable y
            lineGraphView.viewport.setScrollableY(true)

            // on below line we are setting color for series.
            series.color = R.color.purple_200

            // on below line we are adding
            // data series to our graph view.
            lineGraphView.addSeries(series)
        }

    }
    suspend fun getDailyStockDataByApiCall(function:String, symbol:String) {
        val FinanceDataService = RetrofitHelper.getInstance().create(FinanceDataService::class.java)
        Log.d(TAG, function +" "+symbol)
        val stockDataCall = FinanceDataService.getStockData(function,
            symbol, Constants.API_KEY)
        stockDataCall.enqueue(object: Callback<StockData> {
            override fun onResponse(
                call: Call<StockData>,
                response: Response<StockData>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
                dailyStockData = response.body()!!
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
    suspend fun getWeeklyStockDataByApiCall(function:String, symbol:String) {
        val FinanceDataService = RetrofitHelper.getInstance().create(FinanceDataService::class.java)
        Log.d(TAG, function +" "+symbol)
        val stockDataCall = FinanceDataService.getStockData(function,
            symbol, Constants.API_KEY)
        stockDataCall.enqueue(object: Callback<StockData> {
            override fun onResponse(
                call: Call<StockData>,
                response: Response<StockData>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
                weeklyStockData = response.body()!!
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.cause}")
            }
        })
    }
    suspend fun getMonthStockDataByApiCall(function:String, symbol:String) {
        val FinanceDataService = RetrofitHelper.getInstance().create(FinanceDataService::class.java)
        Log.d(TAG, function +" "+symbol)
        val stockDataCall = FinanceDataService.getStockData(function,
            symbol, Constants.API_KEY)
        stockDataCall.enqueue(object: Callback<StockData> {
            override fun onResponse(
                call: Call<StockData>,
                response: Response<StockData>
            ) {
                Log.d(TAG, "onResponse: ${response.body()}")
                monthlyStockData = response.body()!!
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.cause}")
            }
        })
    }
}