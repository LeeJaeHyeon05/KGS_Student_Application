package com.example.kgs_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(var photos: ArrayList<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)) {
        val photos = itemView.findViewById<ImageView>(R.id.slide_imageview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.photos.setImageResource(photos[position])
    }
}