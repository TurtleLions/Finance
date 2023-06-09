package com.example.finance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivity"
        val EXTRA_SEARCH = "search"
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewTitle.text="Type name of stock"
        binding.mainButtonStart.setOnClickListener {
            val intent = Intent(this, PostSearch::class.java)
            intent.putExtra(EXTRA_SEARCH,binding.editTextStocks.text.toString())
            startActivity(intent)
        }

    }
}