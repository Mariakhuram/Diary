package com.mk.diary.adapters.recyclerview

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mk.diary.R
import com.mk.diary.domain.models.ImageItem
import com.mk.mydiary.utils.MyConstants

class BackGroundThemeRecAdapter(private val imageList: List<ImageItem>) :
    RecyclerView.Adapter<BackGroundThemeRecAdapter.ViewHolder>() {
    private lateinit var click:PassRateData
    private var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.background_theme_rec_layout, parent, false)
        return ViewHolder(view)
    }
    interface PassRateData {
        fun clickFunction(modelClass: ImageItem,position: Int)
    }
    fun recyclerClick(listener: PassRateData){
        click=listener
    }
    fun getSelectedItems(): List<ImageItem> {
        return imageList.filter { it.isSelected }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = imageList[position]
        holder.card.setBackgroundResource(if (position == selectedPosition) R.drawable.radius_twelve else 0)
        Glide.with(holder.itemView.context)
            .load(imageItem.uri)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPosition)
            // Update the selected position
            selectedPosition = position

            // Set background for the clicked item
            holder.card.setBackgroundResource(R.drawable.radius_twelve)

            // Notify the adapter that data changed to rebind the views
            notifyDataSetChanged()
            click.clickFunction(imageItem,position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val card: ConstraintLayout = itemView.findViewById(R.id.card)
    }
}
