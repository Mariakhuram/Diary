package com.mk.diary.AdsImplimentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {



    lateinit var mContext: AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        try {


        } catch (ignored: Exception) {
        }
    }

    private fun getCheckLanguage(s: String?): Boolean {
        return when (s) {
            "en" -> {
                true
            }

            "hi" -> {
                true
            }

            else -> "cop" == s
        }
    }
}