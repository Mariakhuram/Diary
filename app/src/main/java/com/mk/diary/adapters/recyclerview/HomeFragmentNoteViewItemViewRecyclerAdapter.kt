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
import com.mk.diary.databinding.HomeNoteviewFragmentReyclerLayoutBinding
import com.mk.diary.databinding.HomeRecyclerItemviewImagesRecBinding
import com.mk.diary.domain.models.ImageItem
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.mydiary.utils.MyConstants

class HomeFragmentNoteViewItemViewRecyclerAdapter(private val modelList: ArrayList<String>) :
    RecyclerView.Adapter<HomeFragmentNoteViewItemViewRecyclerAdapter.ViewHolder>() {

    private lateinit var click: PassRateData
    interface PassRateData {
        fun clickFunction(modelClass: String)
    }

    fun recyclerClick(listener: PassRateData) {
        click = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =HomeRecyclerItemviewImagesRecBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        // Highlight selected items
        if (model!=null){
            holder.binding.imageViewCFour.visibility=View.VISIBLE
            Glide.with(holder.itemView.context).load(model).into(holder.binding.imageViewFour)
        }else{
            holder.binding.imageViewCFour.visibility=View.GONE

        }
    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(val binding: HomeRecyclerItemviewImagesRecBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
