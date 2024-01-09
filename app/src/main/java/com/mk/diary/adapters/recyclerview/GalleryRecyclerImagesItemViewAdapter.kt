package com.mk.diary.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.dialy.dairy.journal.dairywithlock.databinding.GalleryRecyclerRecLayoutBinding

class GalleryRecyclerImagesItemViewAdapter(private var modelList: List<String>) :
    RecyclerView.Adapter<GalleryRecyclerImagesItemViewAdapter.ViewHolder>() {
    private lateinit var click: PassData
    interface PassData {
        fun clickFunction(modelClass:String,position:Int)
    }
    fun recyclerClick(listener: PassData) {
        click = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryRecyclerRecLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        // Highlight selected items

        if (model.isEmpty()){
            holder.binding.imageView.visibility=View.GONE
        }else{
            holder.binding.imageView.visibility=View.VISIBLE
        }
        Glide.with(holder.itemView.context).load(model).into(holder.binding.imageView)
        holder.itemView.setOnClickListener {
            click.clickFunction(model,position)
        }
    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(val binding: GalleryRecyclerRecLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
