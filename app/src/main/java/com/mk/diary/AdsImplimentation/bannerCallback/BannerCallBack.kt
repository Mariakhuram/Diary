package com.example.a4glite.AdsImplimentation.bannerCallback

interface BannerCallBack {
    fun onAdFailedToLoad(adError:String)
    fun onAdLoaded()
    fun onAdImpression()
    fun onPreloaded()
    fun onAdClicked()
    fun onAdClosed()
    fun onAdOpened()
    fun onAdSwipeGestureClicked()
}