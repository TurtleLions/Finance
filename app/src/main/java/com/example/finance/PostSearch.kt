package com.example.finance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.function.Predicate

class PostSearch : AppCompatActivity() {
    companion object{
        const val TAG = "PostSearch"
        val EXTRA_SEARCH = "search"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postsearch)
        val query = intent.getStringExtra(MainActivity.EXTRA_SEARCH)
        val inputStream = resources.openRawResource(R.raw.stock_list)
        val jsonString = inputStream.bufferedReader().use{
            it.readText()
        }
        val gson = Gson()
        val type = object : TypeToken<List<Stock>>() {}.type
        var list = gson.fromJson<List<Stock>>(jsonString, type)

    }
    fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }
}