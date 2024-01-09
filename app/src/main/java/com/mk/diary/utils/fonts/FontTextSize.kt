package com.mk.diary.utils.fonts

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import android.widget.TextView
import com.mk.diary.utils.MyConstants

open class FontTextSize: FontFamily() {

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
        checkKey:String,
        textViews: ArrayList<TextView>? = null,
        editTexts: ArrayList<EditText>? = null
    ) {
        try {
            when(checkKey){
                MyConstants.ALL_CAPITAL->{
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
                MyConstants.ALL_SMALL-> {
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
                MyConstants.ALL_DEFAULT->{
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

