package com.mk.diary.utils.fonts

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment


open class FontFamily : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun setupCustomFonts(
        customTypeface: Typeface?,
        textViews: ArrayList<TextView>? = null,
        editTexts: ArrayList<EditText>? = null
    ) {
        try {
            textViews?.forEach { it.typeface = customTypeface }
            editTexts?.forEach { it.typeface = customTypeface }
        } catch (ex: Exception) {
            println("FontTextSize threw:\n${ex.javaClass.name}\n${ex.stackTraceToString()}")
            throw ex
        }
    }
    fun setTypefaceOfView(view: View?, customTypeface: Typeface?) {
        if (customTypeface != null && view != null) {
            try {
                when (view) {
                    is TextView -> view.typeface = customTypeface
                    is Button -> view.typeface = customTypeface
                    is EditText -> view.typeface = customTypeface
                    is ViewGroup -> setTypefaceOfViewGroup(view, customTypeface)
                    else -> println("AndroidTypefaceUtility: ${view.id} is type of ${view.javaClass} and does not have a typeface property")
                }
            } catch (ex: Exception) {
                println("AndroidTypefaceUtility threw:\n${ex.javaClass.name}\n${ex.stackTraceToString()}")
                throw ex
            }
        } else {
            println("AndroidTypefaceUtility: customTypeface / view parameter should not be null")
        }
    }

    private fun setTypefaceOfViewGroup(layout: ViewGroup?, customTypeface: Typeface?) {
        if (customTypeface != null && layout != null) {
            for (i in 0 until layout.childCount) {
                setTypefaceOfView(layout.getChildAt(i), customTypeface)
            }
        } else {
            println("AndroidTypefaceUtility: customTypeface / layout parameter should not be null")
        }
    }
    fun setColorOnView(color:Int,textViews: ArrayList<TextView>? = null){
        try {
            textViews?.forEach { it.setTextColor(color) }
        }catch (e:Exception){

        }
    }
}
