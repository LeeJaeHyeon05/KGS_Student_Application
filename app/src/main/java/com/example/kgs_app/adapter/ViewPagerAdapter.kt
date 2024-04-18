package com.example.kgs_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.kgs_app.R

class ViewPagerAdapter(var photos: ArrayList<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)) {
        val photos = itemView.findViewById<ImageView>(R.id.slide_imageview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        //setImage로 하면 화질이 안 깨지는 반면 사이즈  옆에 공백 하지만 setBackground 하면 사이즈가 이상해지지만 공백 사라짐
//      holder.photos.setImageResource(photos[position])
        holder.photos.setBackgroundResource(photos[position])
    }
}