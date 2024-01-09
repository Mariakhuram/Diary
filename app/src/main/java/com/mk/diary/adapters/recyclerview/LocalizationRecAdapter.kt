package com.mk.diary.adapters.recyclerview

import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.localization.LangCountryModelClass
import com.mk.diary.localization.SharedPref
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.LocalizationRecyclerLayoutBinding


class LocalizationRecAdapter(private var modelList: List<LangCountryModelClass>) :
    RecyclerView.Adapter<LocalizationRecAdapter.ViewHolder>() {
    private lateinit var click: PassData
    private lateinit var sp :SharedPref
    interface PassData {
        fun clickFunction(modelClass: LangCountryModelClass)
    }
    fun recyclerClick(listener: PassData) {
        click = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LocalizationRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        sp = SharedPref(holder.itemView.context)
        // Highlight selected items
        holder.binding.languageTv.text=model.name
        holder.itemView.setOnClickListener {

            click.clickFunction(model)
        }
        holder.binding.radioBtn.setImageResource(R.drawable.radio_button_checked)
        if (model.name == sp.getLangName()) {
        } else {
            holder.binding.radioBtn.setImageResource(R.drawable.radio_button_unchecked)
        }
    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(val binding: LocalizationRecyclerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
