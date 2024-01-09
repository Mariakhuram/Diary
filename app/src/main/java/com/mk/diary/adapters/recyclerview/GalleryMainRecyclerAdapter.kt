package com.mk.diary.adapters.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.utils.MyConstants
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.GalleryRecyclervewBinding

class GalleryMainRecyclerAdapter(private var modelList: List<NoteViewModelClass>,val  navController:NavController) :
    RecyclerView.Adapter<GalleryMainRecyclerAdapter.ViewHolder>() {

    private val uniqueDatesSet = mutableSetOf<String>() // Keep track of unique dates
    private lateinit var click: PassData

    interface PassData {
        fun clickFunction(modelClass: NoteViewModelClass, position: Int)
    }

    fun recyclerClick(listener: PassData) {
        click = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryRecyclervewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        if (model.date?.let { uniqueDatesSet.add(it) } == true) {
            // If the date is not in the set, add it and update the RecyclerView
            val filteredImages = modelList
                .filter { it.date == model.date }
                .flatMap { it.listOfImages }
            val mAdapter = GalleryRecyclerImagesItemViewAdapter(filteredImages)
            holder.binding.recyclerView.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
            mAdapter.recyclerClick(object : GalleryRecyclerImagesItemViewAdapter.PassData {
                override fun clickFunction(modelClass: String, position: Int) {
                    val bundle = Bundle()
                    bundle.putString(MyConstants.PASS_DATA, modelClass)
                    navController.navigate(R.id.action_galleryFragment_to_noteViewDetailsImageViewFragment, bundle)
                }
            })
            holder.binding.title.text = model.date
            holder.itemView.setOnClickListener {
                click.clickFunction(model, position)
            }
        } else {
            // If the date is already in the set, hide the current item
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }
    override fun getItemCount(): Int {
        return modelList.size
    }
    fun resetAdapter() {
        uniqueDatesSet.clear()
        notifyDataSetChanged()
    }
    class ViewHolder(val binding: GalleryRecyclervewBinding) : RecyclerView.ViewHolder(binding.root)
}

