package com.mk.diary.adapters.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mk.diary.R
import com.mk.diary.domain.models.ImageItem
import com.mk.mydiary.utils.MyConstants

class CreateNoteImageRecyclerAdapter(private val modelList: List<String>) :
    RecyclerView.Adapter<CreateNoteImageRecyclerAdapter.ViewHolder>() {

    private lateinit var click: PassRateData
    interface PassRateData {
        fun clickFunction(modelClass: String)
        fun delete(modelClass: String)
    }

    fun recyclerClick(listener: PassRateData) {
        click = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.create_note_images_rec, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = modelList[position]
        Glide.with(holder.itemView.context).load(item).into(holder.image)
        holder.itemView.setOnClickListener {
            click.clickFunction(item)
        }
        holder.deleteBtn.setOnClickListener {
            click.delete(item)
        }
    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ImageViewThree)
        val deleteBtn: CardView = itemView.findViewById(R.id.crossImageThree)

    }
}
