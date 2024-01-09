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
import com.mk.diary.domain.models.ImageItem
import com.mk.diary.utils.MyConstants
import my.dialy.dairy.journal.dairywithlock.R

class GalleryImagePickerRecAdapter(private val imageList: List<ImageItem>) :
    RecyclerView.Adapter<GalleryImagePickerRecAdapter.ViewHolder>() {

    private lateinit var click: PassRateData
    interface PassRateData {
        fun clickFunction(modelClass: ImageItem, position: Int)
    }

    fun recyclerClick(listener: PassRateData) {
        click = listener
    }
    fun getSelectedItems(): List<ImageItem> {
        return imageList.filter { it.isSelected }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_rec_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = imageList[position]
        // Highlight selected items
        if (position == 0) {
            holder.positionTv.visibility=View.GONE
            holder.cardView.setBackgroundColor(Color.TRANSPARENT)
            val padding = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.imageItemPositionZero)
            holder.imageView.setPadding(padding, padding, padding, padding)
        } else {
            // Reset padding for other positions
            holder.imageView.setPadding(0, 0, 0, 0)
            if (imageItem.isSelected) {
                holder.positionTv.text=(MyConstants.listOfImageCounting.indexOf(imageItem.uri)+1).toString()
               holder.positionTv.visibility = View.VISIBLE
                holder.cardView.setBackgroundResource(R.drawable.radius_twelve)
            } else {
                holder.cardView.setBackgroundColor(Color.TRANSPARENT)
                holder.positionTv.visibility = View.GONE
            }

        }
        Glide.with(holder.itemView.context)
            .load(imageItem.uri)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            if (position==0){
            }else{
                imageItem.isSelected = !imageItem.isSelected
                if (MyConstants.listOfImageCounting.contains(imageItem.uri)){
                    MyConstants.listOfImageCounting.remove(imageItem.uri)
                }else{
                    MyConstants.listOfImageCounting.add(imageItem.uri)
                }
                if (MyConstants.list.contains(imageItem.uri.toString())){
                    MyConstants.list.remove(imageItem.uri.toString())
                }else{
                    MyConstants.list.add(imageItem.uri.toString())
                }
            }
            click.clickFunction(imageItem, position)
            notifyDataSetChanged()
        }
    }
    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val cardView: ConstraintLayout = itemView.findViewById(R.id.card)
        val positionTv: TextView = itemView.findViewById(R.id.positionViewTv)
    }
}
