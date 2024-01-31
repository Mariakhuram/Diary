package com.mk.diary.AdsImplimentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.mk.diary.AdsImplimentation.adsConstants.AdsConstants
import my.dialy.dairy.journal.dairywithlock.R

class BaseCollapsableBannerActivity ( var context: Activity):AppCompatActivity(){
    private var bannerAd: AdView? = null
    private var adFrame: LinearLayout? = null

//    companion object {
        private var isAdLoadCalled: Boolean = false
        private var isRequesting: Boolean = false

        fun loadCollapsableBannerAd(adFrame: LinearLayout,context: Activity) {
            isAdLoadCalled = true
            loadSingleCollapsableBannerAd(adFrame,context)
        }

         fun loadSingleCollapsableBannerAd(adFrame: LinearLayout,context: Activity) {
            if (isAdLoadCalled) {
                try {
                    if (!isRequesting) {
                        isRequesting = true
                        adFrame.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                        try {
                            val collapseBannerAd = AdView(context).apply {
                                this.adUnitId = context.getString(R.string.exit)
                                this.setAdSize(AdsConstants.getAdSizeAds(context))
                                this.loadAd(
                                    AdRequest.Builder().addNetworkExtrasBundle(
                                        AdMobAdapter::class.java,
                                        Bundle().apply {
                                            putString("collapsible", "bottom")
                                        }).build()
                                )
                            }

                            collapseBannerAd.adListener = object : AdListener() {
                                override fun onAdLoaded() {
                                    super.onAdLoaded()
                                    if (context.isFinishing || context.isDestroyed || context.isChangingConfigurations) {
                                        collapseBannerAd.destroy()
                                        return
                                    }
                                    collapseBannerAd.adListener = object : AdListener() {}
                                    adFrame.visibility = View.VISIBLE
                                    adFrame.removeAllViews()
                                    adFrame.addView(collapseBannerAd)
                                }

                                override fun onAdFailedToLoad(p0: LoadAdError) {
                                    super.onAdFailedToLoad(p0)
                                    isRequesting = false
                                    if (context.isFinishing || context.isDestroyed || context.isChangingConfigurations) {
                                        collapseBannerAd.destroy()
                                        return
                                    }
                                    adFrame.removeAllViews()
                                    adFrame.visibility = View.GONE
                                }
                            }
                        } catch (e: Exception) {
                            isRequesting = false
                        } catch (e: OutOfMemoryError) {
                            isRequesting = false
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }
//    }

    override fun onResume() {
        super.onResume()
        if (bannerAd == null) {
            loadCollapsableBannerAd(adFrame!!, context)
        }
        bannerAd?.resume()
    }

    override fun onPause() {
        super.onPause()
        bannerAd?.pause()
    }

    override fun onDestroy() {
        bannerAd?.destroy()
        bannerAd = null
        super.onDestroy()
    }
}
