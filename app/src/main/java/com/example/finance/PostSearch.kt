package com.example.finance

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
    private lateinit var usedList:MutableList<Stock>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val query = intent.getStringExtra(MainActivity.EXTRA_SEARCH)?.uppercase() ?:""
        val inputStream = resources.openRawResource(R.raw.stock_list)
        val jsonString = inputStream.bufferedReader().use{
            it.readText()
        }
        val gson = Gson()
        val type = object : TypeToken<List<Stock>>() {}.type
        var list1 = gson.fromJson<List<Stock>>(jsonString, type).toMutableList()
        var predicate1 = Predicate { stock: Stock -> !stock.ticker.uppercase().contains(query) }
        var newList1 = remove(list1, predicate1)
        var list2 = gson.fromJson<List<Stock>>(jsonString, type).toMutableList()
        var predicate2 = Predicate { stock: Stock -> !stock.name.uppercase().contains(query) }
        var newList2 = remove(list2, predicate2)
        usedList = (newList1+newList2).toMutableList()
        adapter = SearchAdapter(usedList)
        binding.recyclerviewSearchResults.adapter = adapter
        binding.recyclerviewSearchResults.layoutManager = LinearLayoutManager(this)

    }
    fun remove(list: MutableList<Stock>, predicate: Predicate<Stock>):MutableList<Stock> {
        list.removeIf { x: Stock -> predicate.test(x) }
        val newList = list.toMutableList()
        return newList
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.stock_list_menu, menu)

        return true
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_ticker ->{
                adapter.stockList = adapter.stockList.sortedWith(compareBy<Stock> {
                    adapter.stockList[adapter.stockList.indexOf(it)].ticker
                }.thenBy {
                    adapter.stockList[adapter.stockList.indexOf(it)].name
                }.thenBy {
                    adapter.stockList[adapter.stockList.indexOf(it)].exchange
                }).toMutableList()
                adapter.notifyDataSetChanged()
                true
            }
            R.id.menu_name->{
                adapter.stockList = adapter.stockList.sortedWith(compareBy<Stock> {
                    adapter.stockList[adapter.stockList.indexOf(it)].name
                }.thenBy {
                    adapter.stockList[adapter.stockList.indexOf(it)].ticker
                }.thenBy {
                    adapter.stockList[adapter.stockList.indexOf(it)].exchange
                }).toMutableList()
                adapter.notifyDataSetChanged()
                true
            }
            R.id.menu_exchange->{
                adapter.stockList=adapter.stockList.sortedWith(compareBy<Stock> {
                    adapter.stockList[adapter.stockList.indexOf(it)].exchange
                }.thenBy {
                    adapter.stockList[adapter.stockList.indexOf(it)].ticker
                }.thenBy {
                    adapter.stockList[adapter.stockList.indexOf(it)].name
                }).toMutableList()
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}