package com.mk.diary.adapters.recyclerview

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mk.diary.databinding.HomeNoteviewFragmentReyclerLayoutBinding
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.interfaces.ItemClick

class HomeFragmentNoteViewRecyclerAdapter(private val modelList: List<NoteViewModelClass>) :
    RecyclerView.Adapter<HomeFragmentNoteViewRecyclerAdapter.ViewHolder>() {
    private val listOfTextView :ArrayList<TextView>  by lazy { ArrayList() }
    private lateinit var click: PassData
    interface PassData {
        fun clickFunction(modelClass: NoteViewModelClass,position:Int)
    }
    fun recyclerClick(listener: PassData) {
        click = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =HomeNoteviewFragmentReyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        listOfTextView.add(holder.binding.dayTv)
        listOfTextView.add(holder.binding.dateTv)
        listOfTextView.add(holder.binding.monthYearTv)
        listOfTextView.add(holder.binding.descriptionEd)
        // Highlight selected items
        if (model.listOfImages.isEmpty()){
            holder.binding.recyclerView.visibility=View.GONE
        }else{
            holder.binding.recyclerView.visibility=View.VISIBLE
        }
        val mAdapter =HomeFragmentNoteViewItemViewRecyclerAdapter(model.listOfImages)
        holder.binding.recyclerView.adapter=mAdapter
        holder.binding.dateTv.text=model.date
        holder.binding.dayTv.text=model.dayOfWeek
        holder.binding.monthYearTv.text="${model.monthString} ${model.year}"
        if (!model.description.isNullOrEmpty()){
            holder.binding.descriptionEd.visibility=View.VISIBLE
            holder.binding.descriptionEd.text=model.description
        }
        if (model.noteViewStatus!=null){
            holder.binding.noteViewStatusCard.visibility=View.VISIBLE
            holder.binding.noteViewStatusImage.setImageResource(model.noteViewStatus)
        }else{
            holder.binding.noteViewStatusCard.visibility=View.GONE
        }
        //font family
        val customTypeface = if (model.fontFamily != null) {
            Typeface.createFromAsset(holder.itemView.context.assets, model.fontFamily)
        } else {
            null
        }
        setupCustomFonts(customTypeface,holder.binding.dayTv)
        setupCustomFonts(customTypeface,holder.binding.dateTv)
        setupCustomFonts(customTypeface,holder.binding.monthYearTv)
        setupCustomFonts(customTypeface,holder.binding.descriptionEd)
        //color
        val color = if (model.textColor != null) {
            model.textColor
        } else {
            null
        }
        setColorOnEachItem(color, holder.binding.dayTv)
        setColorOnEachItem(color, holder.binding.dateTv)
        setColorOnEachItem(color, holder.binding.monthYearTv)
        setColorOnEachItem(color, holder.binding.descriptionEd)

        holder.itemView.setOnClickListener {
            click.clickFunction(model,position)
        }
    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(val binding: HomeNoteviewFragmentReyclerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    private fun setupCustomFonts(
        customTypeface: Typeface?,
        textView: TextView?
    ) {
        try {
            textView?.typeface = customTypeface
        } catch (ex: Exception) {
            println("FontTextSize threw:\n${ex.javaClass.name}\n${ex.stackTraceToString()}")
            throw ex
        }
    }

    private fun setColorOnEachItem(color: Int?, textView:TextView?){
        try {
            if (color != null) {
                textView?.setTextColor(color)
            }
        }catch (e:Exception){

        }
    }

}
