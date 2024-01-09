package com.mk.diary.adapters.recyclerview


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import my.dialy.dairy.journal.dairywithlock.R

class FontColorBottomSheetRecAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<FontColorBottomSheetRecAdapter.ViewHolder>() {
    private lateinit var click:PassRateData
    private var selectedPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.font_color_rec_layout, parent, false)
        return ViewHolder(view)
    }
    interface PassRateData {
        fun clickFunction(modelClass: Int, position: Int)
    }
    fun recyclerClick(listener: PassRateData){
        click=listener
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val imageItem = imageList[position]
        holder.card.strokeColor = if (position == selectedPosition) holder.itemView.context.resources.getColor(R.color.pinkButtonColor) else 0
        holder.textview.setBackgroundColor(imageItem)
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPosition)
            // Update the selected position
            selectedPosition = position
            holder.card.strokeColor = holder.itemView.context.resources.getColor(R.color.pinkButtonColor)

            // Set background for the clicked item
            // Notify the adapter that data changed to rebind the views
            notifyDataSetChanged()
            click.clickFunction(imageItem,position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textview: TextView = itemView.findViewById(R.id.colorTextView)
        val card: MaterialCardView = itemView.findViewById(R.id.cardColor)
    }
}
