package com.example.a4glite.AdsImplimentation

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mk.diary.AdsImplimentation.AppOpenManager

class StaticClass {
    companion object{
        var typeclick=""
        var mInterstitialAd: InterstitialAd? = null
        private val appOpenManager: AppOpenManager? = null
        var count = 0
        var counter = 3
        var showinsititalornot = false
    }
}