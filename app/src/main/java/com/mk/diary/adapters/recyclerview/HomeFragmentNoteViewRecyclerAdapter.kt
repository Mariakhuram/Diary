package com.mk.diary.adapters.recyclerview

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mk.diary.domain.models.NoteViewModelClass
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.HomeNoteviewFragmentReyclerLayoutBinding


class HomeFragmentNoteViewRecyclerAdapter(private var modelList: List<NoteViewModelClass>) :
    RecyclerView.Adapter<HomeFragmentNoteViewRecyclerAdapter.ViewHolder>() {
    private val listOfTextView :ArrayList<TextView>  by lazy { ArrayList() }
    private lateinit var click: PassData
    private var searchedText:String?=null
    interface PassData {
        fun clickFunction(modelClass: NoteViewModelClass,position:Int)
    }
    fun recyclerClick(listener: PassData) {
        click = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeNoteviewFragmentReyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        if (model.backgroundTheme?.isNotEmpty()==true){
           Glide.with(holder.itemView.context).load(model.backgroundTheme)
               .into(holder.binding.cardBackground)
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
        if (!searchedText.isNullOrEmpty()) {
            if (model.description?.contains(searchedText!!) == true) {
                val spannableString = SpannableStringBuilder(model.description)
                var startIndex = 0
                while (startIndex < model.description.length && startIndex >= 0) {
                    startIndex = model.description.indexOf(searchedText!!, startIndex)
                    if (startIndex >= 0) {
                        val endIndex = startIndex + searchedText!!.length
                        spannableString.setSpan(
                            BackgroundColorSpan(holder.itemView.context.resources.getColor(R.color.pinkButtonColor)),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannableString.setSpan(
                            ForegroundColorSpan(holder.itemView.context.resources.getColor(R.color.black)),
                            startIndex,
                            endIndex,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        startIndex = endIndex
                    }
                }
                holder.binding.descriptionEd.setText(spannableString)
            }
        } else {
            holder.binding.descriptionEd.text = model.description
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

        customTypeface?.let {
            setupCustomFonts(it, holder.binding.dayTv)
            setupCustomFonts(it, holder.binding.dateTv)
            setupCustomFonts(it, holder.binding.monthYearTv)
            setupCustomFonts(it, holder.binding.descriptionEd)
        }

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
    fun filterList(filteredList: ArrayList<NoteViewModelClass>,searchText:String) {
        searchedText=searchText
        modelList = filteredList
        notifyDataSetChanged()
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
