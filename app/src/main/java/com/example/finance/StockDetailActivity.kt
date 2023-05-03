package com.example.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finance.databinding.ActivityStockDetailBinding

class StockDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockDetailBinding
    private lateinit var stock: Stock
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
}