package com.example.finance

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.finance.databinding.ActivityStockDetailBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jjoe64.graphview.GraphView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        binding.buttonTimeGraph.isClickable = false
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
            onButton()

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
            onButton()
            binding.buttonTimeGraph.isClickable = true

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
    fun onButton(){
        Log.d("Pain", dailyStockData.dailyTimeSeries!!.toList().toString())
        Log.d("Pain", weeklyStockData.weeklyTimeSeries!!.toList().toString())
        Log.d("Pain", monthlyStockData.monthlyTimeSeries!!.toList().toString())
        val dayData = dailyStockData.dailyTimeSeries!!.toList()
        val weekData = weeklyStockData.weeklyTimeSeries!!.toList()
        val monthData = monthlyStockData.monthlyTimeSeries!!.toList()
        val mutableEntries = mutableListOf<Entry>()
        val dateArray = mutableListOf<String>()
        if(timeState==0){
            val lastDayPeriod = dayData.slice(0..7).reversed()
            var i = 0
            for (data in lastDayPeriod) {
                // turn your data into Entry objects
                Log.d(TAG, data.first)
                Log.d(TAG, dailyStockData.dailyTimeSeries!!.toString())
                Log.d(TAG, dailyStockData.dailyTimeSeries!!.get(data.first)!!.toString())
                Log.d(TAG, dailyStockData.dailyTimeSeries!!.get(data.first)!!.get("2. high")!!)
                val entry = Entry(i.toFloat(), dailyStockData.dailyTimeSeries!!.get(data.first)!!.get("2. high")!!.toFloat() )
                mutableEntries.add(entry)
                dateArray.add(data.first.substring(5))
                i+=1
            }
        }
        else if(timeState==1){
            val lastWeekPeriod = weekData.slice(0..7).reversed()
            var i = 0
            for (data in lastWeekPeriod) {
                // turn your data into Entry objects
                Log.d(TAG, data.first)
                Log.d(TAG, weeklyStockData.weeklyTimeSeries!!.toString())
                Log.d(TAG, weeklyStockData.weeklyTimeSeries!!.get(data.first)!!.toString())
                Log.d(TAG, weeklyStockData.weeklyTimeSeries!!.get(data.first)!!.get("2. high")!!)
                val entry = Entry(i.toFloat(), weeklyStockData.weeklyTimeSeries!!.get(data.first)!!.get("2. high")!!.toFloat() )
                mutableEntries.add(entry)
                dateArray.add(data.first.substring(5))
                i+=1
            }

        }
        else if(timeState==2){
            val lastWeekPeriod = monthData.slice(0..7).reversed()
            var i = 0
            for (data in lastWeekPeriod) {
                // turn your data into Entry objects
                Log.d(TAG, data.first)
                Log.d(TAG, monthlyStockData.monthlyTimeSeries!!.toString())
                Log.d(TAG, monthlyStockData.monthlyTimeSeries!!.get(data.first)!!.toString())
                Log.d(TAG, monthlyStockData.monthlyTimeSeries!!.get(data.first)!!.get("2. high")!!)
                val entry = Entry(i.toFloat(), monthlyStockData.monthlyTimeSeries!!.get(data.first)!!.get("2. high")!!.toFloat() )
                mutableEntries.add(entry)
                dateArray.add(data.first.substring(5))
                i+=1
            }
        }
        val dataSet = LineDataSet(mutableEntries,"Label")
        val lineData = LineData(dataSet)
        binding.stockGraph.setData(lineData)
        binding.stockGraph.legend.isEnabled = false
        binding.stockGraph.description.isEnabled = false
        binding.stockGraph.isDragEnabled = false
        binding.stockGraph.xAxis.valueFormatter = IndexAxisValueFormatter(dateArray)
        binding.stockGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.stockGraph.xAxis.labelRotationAngle = 45.toFloat()
        binding.stockGraph.extraBottomOffset = 20.toFloat()
        binding.stockGraph.invalidate()
        binding.stockGraph.xAxis.labelRotationAngle = 90.toFloat()
        binding.stockGraph.isDragEnabled = false
        binding.stockGraph.xAxis.valueFormatter = IndexAxisValueFormatter()
    }
}