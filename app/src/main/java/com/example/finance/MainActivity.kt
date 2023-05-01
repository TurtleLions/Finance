package com.example.finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        setContentView(R.layout.activity_main)

        binding.mainButtonStart.setOnClickListener {
            val intent = Intent(this, PostSearch::class.java)
            intent.putExtra(EXTRA_SEARCH,binding.editTextStocks.text.toString())
            startActivity(intent)
        }

    }
}