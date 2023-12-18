package com.mk.diary.utils.fonts

import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.toHtml
import androidx.core.widget.addTextChangedListener
import com.mk.diary.databinding.FragmentCreateNoteBinding
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.fonts.FontFamily

open class FontTextSize:FontFamily() {

    fun setupTextSizeOnViews(
        textSize: Float,
        textViews: ArrayList<TextView>? = null,
        editTexts: ArrayList<EditText>? = null
    ) {
        try {
            when(textSize){
                12f->{
                    textViews?.get(0)?.textSize=18f
                    textViews?.get(1)?.textSize=12f
                    textViews?.get(2)?.textSize=12f
                    editTexts?.get(0)?.textSize=17f
                    editTexts?.get(1)?.textSize=14f
                }
                16f->{
                    textViews?.get(0)?.textSize=23f
                    textViews?.get(1)?.textSize=17f
                    textViews?.get(2)?.textSize=17f
                    editTexts?.get(0)?.textSize=23f
                    editTexts?.get(1)?.textSize=17f
                }
                20f->{
                    textViews?.get(0)?.textSize=30f
                    textViews?.get(1)?.textSize=20f
                    textViews?.get(2)?.textSize=20f
                    editTexts?.get(0)?.textSize=26f
                    editTexts?.get(1)?.textSize=20f
                }
            }
        } catch (ex: Exception) {
            println("FontTextSize threw:\n${ex.javaClass.name}\n${ex.stackTraceToString()}")
            throw ex
        }
    }
    fun setupTextSizeOnForPreview(
        textSize: Float,
        textViews: ArrayList<TextView>? = null,
    ) {
        try {
            when(textSize){
                12f->{
                    textViews?.get(0)?.textSize=18f
                    textViews?.get(1)?.textSize=12f
                    textViews?.get(2)?.textSize=12f
                    textViews?.get(3)?.textSize=17f
                }
                16f->{
                    textViews?.get(0)?.textSize=23f
                    textViews?.get(1)?.textSize=17f
                    textViews?.get(2)?.textSize=17f
                    textViews?.get(3)?.textSize=23f
                }
                20f->{
                    textViews?.get(0)?.textSize=30f
                    textViews?.get(1)?.textSize=20f
                    textViews?.get(2)?.textSize=20f
                    textViews?.get(3)?.textSize=26f
                }
            }
        } catch (ex: Exception) {
            println("FontTextSize threw:\n${ex.javaClass.name}\n${ex.stackTraceToString()}")
            throw ex
        }
    }

    fun setupTextLettersCustomizationOnViews(
        checkKey:Boolean,
        textViews: ArrayList<TextView>? = null,
        editTexts: ArrayList<EditText>? = null
    ) {
        try {
            when(checkKey){
                true->{
                    textViews?.forEach { it.isAllCaps=true }
                    editTexts?.get(0)?.filters = arrayOf(object : InputFilter.AllCaps() {
                        override fun filter(
                            source: CharSequence?,
                            start: Int,
                            end: Int,
                            dest: Spanned?,
                            dstart: Int,
                            dend: Int
                        ): CharSequence {
                            return source?.toString()?.uppercase() ?: ""
                        }
                    })
                    editTexts?.get(1)?.filters = arrayOf(object : InputFilter.AllCaps() {
                        override fun filter(
                            source: CharSequence?,
                            start: Int,
                            end: Int,
                            dest: Spanned?,
                            dstart: Int,
                            dend: Int
                        ): CharSequence {
                            return source?.toString()?.uppercase() ?: ""
                        }
                    })
                }
                false-> {
                    textViews?.forEach { it.isAllCaps = false }
                    editTexts?.get(0)?.filters = arrayOf(object : InputFilter.AllCaps() {
                        override fun filter(
                            source: CharSequence?,
                            start: Int,
                            end: Int,
                            dest: Spanned?,
                            dstart: Int,
                            dend: Int
                        ): CharSequence {
                            return source?.toString()?.toLowerCase() ?: ""
                        }
                    })
                    editTexts?.get(1)?.filters = arrayOf(object : InputFilter.AllCaps() {
                        override fun filter(
                            source: CharSequence?,
                            start: Int,
                            end: Int,
                            dest: Spanned?,
                            dstart: Int,
                            dend: Int
                        ): CharSequence {
                            return source?.toString()?.toLowerCase() ?: ""
                        }
                    })
                }
            }
        } catch (ex: Exception) {
            println("FontTextSize threw:\n${ex.javaClass.name}\n${ex.stackTraceToString()}")
            throw ex
        }
    }
}

