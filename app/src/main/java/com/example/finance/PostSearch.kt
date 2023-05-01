package com.example.finance

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finance.databinding.ActivityPostsearchBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.function.Predicate

class PostSearch : AppCompatActivity() {
    companion object{
        const val TAG = "PostSearch"
        val EXTRA_SEARCH = "search"
    }
    private lateinit var binding:ActivityPostsearchBinding
    private lateinit var adapter:SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "post star")
        super.onCreate(savedInstanceState)
        binding = ActivityPostsearchBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_postsearch)
        val query = intent.getStringExtra(MainActivity.EXTRA_SEARCH)?:""
        val inputStream = resources.openRawResource(R.raw.stock_list)
        val jsonString = inputStream.bufferedReader().use{
            it.readText()
        }
        val gson = Gson()
        val type = object : TypeToken<List<Stock>>() {}.type
        var list1 = gson.fromJson<List<Stock>>(jsonString, type).toMutableList()
        var predicate1 = Predicate { stock: Stock -> !stock.ticker.contains(query) }
        var newList1 = remove(list1, predicate1)
        var list2 = gson.fromJson<List<Stock>>(jsonString, type).toMutableList()
        var predicate2 = Predicate { stock: Stock -> !stock.name.contains(query) }
        var newList2 = remove(list2, predicate2)
        var usedList = (newList1+newList2).toMutableList()
        adapter = SearchAdapter(usedList)
        binding.recyclerviewSearchResults.adapter = adapter
        binding.recyclerviewSearchResults.layoutManager = LinearLayoutManager(this)

    }
    fun remove(list: MutableList<Stock>, predicate: Predicate<Stock>):MutableList<Stock> {
        list.removeIf { x: Stock -> predicate.test(x) }
        val newList = list.toMutableList()
        return newList
    }
}