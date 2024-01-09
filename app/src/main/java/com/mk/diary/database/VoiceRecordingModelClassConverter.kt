package com.mk.diary.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mk.diary.domain.models.VoiceRecordingModelClass

class VoiceRecordingModelClassConverter {
    @TypeConverter
    fun fromListToJson(value: ArrayList<VoiceRecordingModelClass>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromJsonToList(value: String?): ArrayList<VoiceRecordingModelClass>? {
        val type = object : TypeToken<ArrayList<VoiceRecordingModelClass>>() {}.type
        return Gson().fromJson(value, type)
    }
}
