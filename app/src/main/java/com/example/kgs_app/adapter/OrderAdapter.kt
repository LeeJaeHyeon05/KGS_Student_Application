package com.example.kgs_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.kgs_app.R
import com.example.kgs_app.data.OrderData

class OrderAdapter(val itemList: MutableList<OrderData>): RecyclerView.Adapter<OrderAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.time.text = itemList[position].time
        holder.date.text = itemList[position].date
        holder.count.text = itemList[position].count
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.orderName)
        val time: TextView = itemView.findViewById(R.id.orderTime)
        val date: TextView = itemView.findViewById(R.id.orderDate)
        val count: TextView = itemView.findViewById(R.id.orderCount)
    }
}