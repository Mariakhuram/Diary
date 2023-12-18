package com.mk.diary.database

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringArrayListConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String> = if (value == null) {
        ArrayList()
    } else {
        // Gson deserialization to directly obtain the ArrayList of strings
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        // Use Gson or another JSON library to convert the list to a string
        val jsonString = Gson().toJson(list)
        return jsonString
    }
}

