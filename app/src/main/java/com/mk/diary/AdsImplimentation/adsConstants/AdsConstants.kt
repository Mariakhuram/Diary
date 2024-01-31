package com.mk.diary.AdsImplimentation.adsConstants

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowMetrics
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd

object AdsConstants {
    var mAppOpenAd: AppOpenAd? = null
    var mInterstitialAd: InterstitialAd? = null
    var adMobPreloadNativeAd: NativeAd? = null
    var isInterstitialLoading = false
    var isNativeLoading = false
    fun reset(){
        mAppOpenAd = null
        mInterstitialAd = null
        adMobPreloadNativeAd?.destroy()
        adMobPreloadNativeAd = null
        isInterstitialLoading = false
        isNativeLoading = false
    }
    fun getAdSizeAds(activity: Activity): AdSize {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = activity.windowManager.currentWindowMetrics
            val bounds: Rect = windowMetrics.bounds
            val adWidthPixels = bounds.width()
            val density: Float = activity.resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        } else {
            val display = activity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val widthPixels = outMetrics.widthPixels.toFloat()
            val density = outMetrics.density
            val adWidth = (widthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        }
    }
}