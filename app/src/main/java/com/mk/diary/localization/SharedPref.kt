package com.mk.diary.localization

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("CalculatorPrefs", Context.MODE_PRIVATE)

    fun setLangName(v: String) {
        val editor = sharedPreferences.edit()
        editor.putString("langName", v)
        editor.apply()
    }

    fun getLangName(): String? {
        return sharedPreferences.getString("langName", "English")
    }
    fun clearAllLanguages(){
        val ed=sharedPreferences.edit()
        ed.clear()
        ed.apply()
    }
}