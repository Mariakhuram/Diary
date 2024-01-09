package com.mk.diary.adapters.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.dialy.dairy.journal.dairywithlock.R

class AppThemeImagesSliderAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<AppThemeImagesSliderAdapter.ViewHolder>() {
    lateinit var click:PageItem
    interface PageItem{
        fun currentPage(currentItem:String,position: Int)
    }
    fun viewPagerClick(lis:PageItem){
        click=lis
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_viewpager_layout, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=imageList[position]
        Glide.with(holder.itemView.context).load(item).into(holder.imageView)
    }
    override fun getItemCount(): Int {
        return imageList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
