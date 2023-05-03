package com.example.finance

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SearchAdapter(var stockList: MutableList<Stock>):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    companion object{
        val TAG = "Search Adapter"
        val EXTRA_CURRENTSTOCK = "current stock"
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewSymbol: TextView
        val textViewExchange: TextView
        val layout: ConstraintLayout

        init {
            textViewName = view.findViewById(R.id.item_name)
            textViewSymbol = view.findViewById(R.id.item_symbol)
            textViewExchange = view.findViewById(R.id.item_exchange)
            layout = view.findViewById(R.id.ConstraintLayout)
        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_search_result, viewGroup, false)

        return ViewHolder(view)
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.textViewName.context
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textViewName.text = stockList[position].name
        viewHolder.textViewSymbol.text = stockList[position].ticker
        viewHolder.textViewExchange.text = stockList[position].exchange
        viewHolder.layout.setOnClickListener {
            val detailActivity = Intent(it.context, StockDetailActivity::class.java).apply {
                putExtra(EXTRA_CURRENTSTOCK, stockList[position])
            }
            it.context.startActivity(detailActivity)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = stockList.size
}