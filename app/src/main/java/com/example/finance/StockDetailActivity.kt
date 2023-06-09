package com.example.finance

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.finance.databinding.ActivityStockDetailBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class StockDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockDetailBinding
    private lateinit var stock: Stock
    private lateinit var dailyStockData:StockData
    private lateinit var weeklyStockData:StockData
    private lateinit var monthlyStockData:StockData
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
        binding.stockGraph.isClickable = false
        binding.stockGraph.isVisible = false
        binding.buttonTimeGraph.isClickable=false
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
            if(Looper.myLooper()==null){
                Looper.prepare()
            }

            async {
                getDailyStockDataByApiCall(Constants.DAILY, binding.detailSymbol.text.toString())
                getWeeklyStockDataByApiCall(Constants.WEEKLY, binding.detailSymbol.text.toString())
                getMonthStockDataByApiCall(Constants.MONTHLY, binding.detailSymbol.text.toString())
            }
            while (!this@StockDetailActivity::dailyStockData.isInitialized || !this@StockDetailActivity::weeklyStockData.isInitialized || !this@StockDetailActivity::monthlyStockData.isInitialized) {

            }
            if(dailyStockData.metadata!=null&&weeklyStockData.metadata!=null&&monthlyStockData.metadata!=null){
                onButton()
                binding.buttonTimeGraph.isClickable = true
            }
            else{
                Toast.makeText(this@StockDetailActivity, "Please Try Again Later", Toast.LENGTH_LONG).show()
            }
            runOnUiThread {
                binding.stockGraph.isClickable = true
                binding.stockGraph.isVisible = true
            }
        }

    }
    suspend fun getDailyStockDataByApiCall(function:String, symbol:String) {
        val FinanceDataService = RetrofitHelper.getInstance().create(FinanceDataService::class.java)
        val stockDataCall = FinanceDataService.getStockData(function,
            symbol, Constants.API_KEY)
        stockDataCall.enqueue(object: Callback<StockData> {
            override fun onResponse(
                call: Call<StockData>,
                response: Response<StockData>
            ) {
                dailyStockData = response.body()!!
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }
    suspend fun getWeeklyStockDataByApiCall(function:String, symbol:String) {
        val FinanceDataService = RetrofitHelper.getInstance().create(FinanceDataService::class.java)
        val stockDataCall = FinanceDataService.getStockData(function,
            symbol, Constants.API_KEY)
        stockDataCall.enqueue(object: Callback<StockData> {
            override fun onResponse(
                call: Call<StockData>,
                response: Response<StockData>
            ) {
                weeklyStockData = response.body()!!
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.cause}")
            }
        })
    }
    suspend fun getMonthStockDataByApiCall(function:String, symbol:String) {
        val FinanceDataService = RetrofitHelper.getInstance().create(FinanceDataService::class.java)
        val stockDataCall = FinanceDataService.getStockData(function,
            symbol, Constants.API_KEY)
        stockDataCall.enqueue(object: Callback<StockData> {
            override fun onResponse(
                call: Call<StockData>,
                response: Response<StockData>
            ) {
                monthlyStockData = response.body()!!
            }

            override fun onFailure(call: Call<StockData>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.cause}")
            }
        })
    }
    fun onButton(){
        val dayData = dailyStockData.dailyTimeSeries!!.toList()
        val weekData = weeklyStockData.weeklyTimeSeries!!.toList()
        val monthData = monthlyStockData.monthlyTimeSeries!!.toList()
        val mutableEntries = mutableListOf<Entry>()
        val dateArray = mutableListOf<String>()
        if(timeState==0){
            val lastDayPeriod = dayData.slice(0..7).reversed()
            var i = 0
            for (data in lastDayPeriod) {
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
                val entry = Entry(i.toFloat(), monthlyStockData.monthlyTimeSeries!!.get(data.first)!!.get("2. high")!!.toFloat() )
                mutableEntries.add(entry)
                dateArray.add(data.first.substring(5))
                i+=1
            }
        }
        val dataSet = LineDataSet(mutableEntries,"Label")
        val lineData = LineData(dataSet)
        lineData.setValueTextSize(10f)
        binding.stockGraph.data = lineData
        binding.stockGraph.legend.isEnabled = false
        binding.stockGraph.description.isEnabled = false
        binding.stockGraph.isDragEnabled = false
        binding.stockGraph.setScaleEnabled(false)
        binding.stockGraph.xAxis.valueFormatter = IndexAxisValueFormatter(dateArray)
        binding.stockGraph.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.stockGraph.xAxis.labelRotationAngle = 45f
        binding.stockGraph.extraBottomOffset = 20f
        binding.stockGraph.invalidate()
    }
}