package com.mk.diary.AdsImplimentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.BanneradShimmarLayoutBinding

class BannerAdClass {
    fun show_bannerAd(
        activit: Activity,
        context: Context,
        binding: BanneradShimmarLayoutBinding,
        status: Int
    ) {
        if (status == 1) {
//            // Create an AdView
//          AdView  adView = new AdView(activit);
//            adView.setAdUnitId("ca-app-pub-6005767237808804/2457598523");
//            adView.setAdSize(AdSize.BANNER);
//
//            // Find the layout where you want to display the ad
//            FrameLayout adContainer = findViewById(R.id.ad_container);
//
//            // Add the adView to the layout
//            adContainer.addView(adView);
//
//            // Load the ad
//            val s = SharedPrefRemote()
//            var adidR = s.remoteValue("bannerId", context)
//            if (adidR == "") {
//                adidR = activit.getString(R.string.banner_ad_id)
//             //   Toast.makeText(activit, " if adid"+adidR, Toast.LENGTH_SHORT).show();
//            }
            binding.root.visibility = View.VISIBLE
            binding.shimmerlayout.startShimmer()
            val adView = AdView(context)
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = activit.getString(R.string.addAudioFiles)
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    binding.shimmerlayout.stopShimmer()
                    binding.shimmerlayout.visibility = View.GONE
                    binding.banneradView.visibility = View.VISIBLE
                    binding.banneradView.removeAllViews()
                    binding.banneradView.addView(adView)
                    // Toast.makeText(activit, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    Log.i("shownativead", "banner set success 123")
                    // Toast.makeText(activit, "onAdLoaded", Toast.LENGTH_SHORT).show();
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    //Toast.makeText(activit, "ad clcik", Toast.LENGTH_SHORT).show();
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    //Toast.makeText(activit, "ad closed", Toast.LENGTH_SHORT).show();
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.i("shownativead", "banner set success 666")
                    // Toast.makeText(activit, "ad onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (status == 2) {
            val s = SharedPrefRemote()
            var adidR = s.remoteValue("bannerId", activit)
            if (adidR == "") {
                adidR = context.getString(R.string.exit)
                //Toast.makeText(activit, " if adid"+adidR, Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(activit,"2 m  aya ="+status,Toast.LENGTH_LONG).show();
            binding.root.visibility = View.VISIBLE
            binding.shimmerlayout.startShimmer()
            val adView = AdView(context)
            adView.setAdSize(AdSize.BANNER)
            adView.adUnitId = adidR
            //            AdRequest adRequest = new AdRequest.Builder().build();
//            adView.loadAd(adRequest);
            val extras = Bundle()
            extras.putString("collapsible", "top")
            val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()
            adView.loadAd(adRequest)
            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    binding.shimmerlayout.stopShimmer()
                    binding.shimmerlayout.visibility = View.GONE
                    binding.banneradView.visibility = View.VISIBLE
                    binding.banneradView.removeAllViews()
                    binding.banneradView.addView(adView)
                    // Toast.makeText(activit, "onAdLoaded", Toast.LENGTH_SHORT).show();
                    Log.i("shownativead", "banner set success 123")
                    // Toast.makeText(activit, "onAdLoaded", Toast.LENGTH_SHORT).show();
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    // Toast.makeText(activit, "ad clcik", Toast.LENGTH_SHORT).show();
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    //Toast.makeText(activit, "ad closed", Toast.LENGTH_SHORT).show();
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    // Toast.makeText(activit, "ad onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            binding.root.visibility = View.GONE
        }
    }
}