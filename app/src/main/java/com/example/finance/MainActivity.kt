package com.example.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //lol2
        setContentView(R.layout.activity_main)

    }
}