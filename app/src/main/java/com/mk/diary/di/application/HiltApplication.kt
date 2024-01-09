package com.mk.diary.di.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val ONESIGNAL_APP_ID = "7b449f05-5821-4f95-916f-f74b19a16e81"
@HiltAndroidApp
class HiltApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        OneSignal.Debug.logLevel = LogLevel.VERBOSE
//        // OneSignal Initialization
        OneSignal.initWithContext(this@HiltApplication, ONESIGNAL_APP_ID)
            CoroutineScope(Dispatchers.IO).launch {
                OneSignal.Notifications.requestPermission(true)
        }

    }

}