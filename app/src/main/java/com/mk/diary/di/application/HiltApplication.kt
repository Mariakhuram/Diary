package com.mk.diary.di.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.ads.MobileAds
import com.mk.diary.AdsImplimentation.AppOpenAdX
import com.mk.diary.password.ShowPasswordScreen
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

const val ONESIGNAL_APP_ID = "7b449f05-5821-4f95-916f-f74b19a16e81"
@HiltAndroidApp
class HiltApplication:Application(),LifecycleObserver {
    companion object{
        var showPasswordScreen = true
    }
    override fun onCreate() {
        super.onCreate()
       MobileAds.initialize(this){

        }
        AppOpenAdX(this)

        ShowPasswordScreen(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        OneSignal.promptForPushNotifications()

    }
}