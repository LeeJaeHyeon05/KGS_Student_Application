package com.example.kgs_app.adapter

import android.app.LauncherActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kgs_app.R
import com.example.kgs_app.data.TeacherData

class TeacherRecyclerAdapter(private val itemList: ArrayList<TeacherData>): RecyclerView.Adapter<TeacherRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_teacher, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.number.text = itemList[position].number
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val number: TextView = itemView.findViewById(R.id.number)
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener : OnItemClickListener
}