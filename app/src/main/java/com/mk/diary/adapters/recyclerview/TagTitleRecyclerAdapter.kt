package com.mk.diary.adapters.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mk.diary.R
import com.mk.diary.domain.models.ImageItem
import com.mk.mydiary.utils.MyConstants

class TagTitleRecyclerAdapter(private val modelList: List<String>) :
    RecyclerView.Adapter<TagTitleRecyclerAdapter.ViewHolder>() {

    private lateinit var click: PassRateData
    interface PassRateData {
        fun clickFunction(modelClass: ImageItem, position: Int)
    }

    fun recyclerClick(listener: PassRateData) {
        click = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hash_tag_rec_details_itemview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = modelList[position]
        if (position == 0) {
            holder.tv.visibility = View.GONE
        } else {
            if (item.isNotEmpty()) {
                holder.tv.visibility = View.VISIBLE
                holder.tv.text = item
            } else {
                holder.tv.visibility = View.GONE
            }
        }
    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tagTextViewTv)
    }
}
