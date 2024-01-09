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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mk.diary.domain.models.NoteViewModelClass
import com.mk.diary.domain.models.VoiceRecordingModelClass
import com.mk.diary.localization.LangCountryModelClass
import com.mk.diary.localization.SharedPref
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.LocalizationRecyclerLayoutBinding
import my.dialy.dairy.journal.dairywithlock.databinding.VoiceRecordingRecBinding


class VoiceRecodingRecAdapter(private var modelList: List<VoiceRecordingModelClass>) :
    RecyclerView.Adapter<VoiceRecodingRecAdapter.ViewHolder>() {
    private lateinit var click: PassData

    interface PassData {
        fun clickFunction(modelClass: VoiceRecordingModelClass,position: Int,imageView: ImageView)
        fun deleteFunction(modelClass: VoiceRecordingModelClass)
    }
    fun recyclerClick(listener: PassData) {
        click = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VoiceRecordingRecBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        // Highlight selected items
        holder.binding.voiceDuration.text=model.duration
        holder.binding.voiceName.text=model.voiceName
        holder.itemView.setOnClickListener {
            click.clickFunction(model,position,holder.binding.playPauseBtn)
        }
        holder.binding.voiceDeleteBtn.setOnClickListener {
            click.deleteFunction(model)
        }

    }
    override fun getItemCount(): Int {
        return modelList.size
    }

    class ViewHolder(val binding: VoiceRecordingRecBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
