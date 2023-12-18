package com.mk.mydiary.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mk.diary.R


object SharedPrefObj {
    const val URI_LIST = "uri_list"
    const val USER_TOKEN = "uskjkljljer_token"
    fun saveAuthToken(context: Context, token: String) {
        saveString(context, USER_TOKEN, token)
    }
    fun getToken(context: Context): String? {
        return getString(context, USER_TOKEN)
    }
    fun saveString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences("adfasd", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun removeString(context: Context, key: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences("adfasd", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }
    fun saveUriList(context: Context, uriList: List<String>) {
        val prefs: SharedPreferences = context.getSharedPreferences("adfasd", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        // Convert the list to a JSON string before saving
        val uriListJson = Gson().toJson(uriList)
        editor.putString(URI_LIST, uriListJson)

        editor.apply()
    }

    // Retrieve a list of URIs from SharedPreferences
    fun getUriList(context: Context): ArrayList<String> {
        val prefs: SharedPreferences = context.getSharedPreferences("adfasd", Context.MODE_PRIVATE)

        // Retrieve the JSON string and convert it back to a list
        val uriListJson = prefs.getString(URI_LIST, null)
        return if (uriListJson != null) {
            Gson().fromJson(uriListJson, object : TypeToken<ArrayList<String>>() {}.type)
        } else {
            // If the JSON string is null, return an empty ArrayList
            ArrayList()
        }
    }

    // Remove a URI at a specific index from the list
    fun removeUriAtIndex(context: Context, indexToRemove: Int) {
        // Retrieve the existing list of URIs
        val existingUriList = getUriList(context)

        // Check if the index is within the valid range
        if (indexToRemove >= 0 && indexToRemove < existingUriList.size) {
            // Remove the URI at the specified index
            existingUriList.removeAt(indexToRemove)

            // Save the updated list back to SharedPreferences
            saveUriList(context, existingUriList)
        } else {
            // Handle an invalid index if needed (e.g., show an error message)
        }
    }
    fun getString(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences("adfasd", Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }
    fun saveBoolean(context: Context, key: String, value: Boolean) {
        val prefs: SharedPreferences =
            context.getSharedPreferences("adfasd", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun removeBoolean(context: Context, key: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        val prefs: SharedPreferences =
            context.getSharedPreferences("adfasd", Context.MODE_PRIVATE)
        return prefs.getBoolean(key, defaultValue)
    }
    fun clearData(context: Context){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
    fun saveInt(context: Context, key: String, value: Int) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun removeInt(context: Context, key: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getInt(key, defaultValue)
    }
    fun saveFloat(context: Context, key: String, value: Float) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun removeFloat(context: Context, key: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()
    }

    fun getFloat(context: Context, key: String, defaultValue: Float): Float {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getFloat(key, defaultValue)
    }
}