package com.mk.diary.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.dialy.dairy.journal.dairywithlock.R

class NoteViewDetailsImageViewRecyclerAdapter(private val modelList: List<String>) :
    RecyclerView.Adapter<NoteViewDetailsImageViewRecyclerAdapter.ViewHolder>() {

    private lateinit var click: PassRateData
    interface PassRateData {
        fun clickFunction(modelClass: String)
    }

    fun recyclerClick(listener: PassRateData) {
        click = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.noteview_details_images_rec, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = modelList[position]
        Glide.with(holder.itemView.context).load(item).into(holder.image)
        holder.itemView.setOnClickListener {
            click.clickFunction(item)
        }

    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ImageViewThree)
    }
}
