package com.example.finance

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.finance.databinding.ActivityStockDetailBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


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
            val formatter = SimpleDateFormat("MM-dd")
            val one = Instant.now()
            val oneDate: Date = Date.from(one)
            val formattedOneDate = formatter.format(oneDate)
            val two = Instant.now().minus(1, ChronoUnit.DAYS)
            val twoDate: Date = Date.from(two)
            val formattedTwoDate = formatter.format(twoDate)
            val three = Instant.now().minus(2, ChronoUnit.DAYS)
            val threeDate: Date = Date.from(three)
            val formattedThreeDate = formatter.format(threeDate)
            val four = Instant.now().minus(3, ChronoUnit.DAYS)
            val fourDate: Date = Date.from(four)
            val formattedFourDate = formatter.format(fourDate)
            val five = Instant.now().minus(4, ChronoUnit.DAYS)
            val fiveDate: Date = Date.from(five)
            val formattedFiveDate = formatter.format(fiveDate)
            val six = Instant.now().minus(5, ChronoUnit.DAYS)
            val sixDate: Date = Date.from(six)
            val formattedSixDate = formatter.format(sixDate)
            val seven = Instant.now().minus(6, ChronoUnit.DAYS)
            val sevenDate: Date = Date.from(seven)
            val formattedSevenDate = formatter.format(sevenDate)
            Log.d(TAG, one.toString())
            Log.d(TAG, two.toString())
            Log.d(TAG, formattedOneDate)
            Log.d(TAG, formattedTwoDate)


            val series: LineGraphSeries<DataPoint> = LineGraphSeries(
                arrayOf(
                    // on below line we are adding
                    // each point on our x and y axis.
                    //dailyStockData.dailyTimeSeries?.get("$")?.get("2. high")
                    //?.let { DataPoint(0.0, it.toDouble()) },
                    DataPoint(sevenDate, 3.0),
                    DataPoint(sixDate, 4.0),
                    DataPoint(fiveDate, 9.0),
                    DataPoint(fourDate, 6.0),
                    DataPoint(threeDate, 3.0),
                    DataPoint(twoDate, 6.0),
                    DataPoint(oneDate, 1.0)
                )
            )
            lineGraphView.addSeries(series)

// set manual x bounds to have nice steps
            lineGraphView.viewport.setMinX(sevenDate.time.toDouble())
            lineGraphView.viewport.setMaxX(oneDate.time.toDouble())
            val staticLabelsFormatter = StaticLabelsFormatter(lineGraphView)
            staticLabelsFormatter.setHorizontalLabels(arrayOf(formattedSevenDate,formattedSixDate,formattedFiveDate,formattedFourDate,formattedThreeDate,formattedTwoDate,formattedOneDate))
            lineGraphView.gridLabelRenderer.labelFormatter = staticLabelsFormatter
            lineGraphView.gridLabelRenderer.labelsSpace = 60
            lineGraphView.gridLabelRenderer.padding = 60
            lineGraphView.gridLabelRenderer.setHorizontalLabelsAngle(45)
            lineGraphView.viewport.isXAxisBoundsManual = true
            lineGraphView.viewport.setMinY(0.toDouble())
            lineGraphView.viewport.isYAxisBoundsManual =true


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