package com.mk.diary.AdsImplimentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.util.Log

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mk.diary.AdsImplimentation.CounterJavaClass.count
import com.mk.diary.AdsImplimentation.CounterJavaClass.counter
import my.dialy.dairy.journal.dairywithlock.R


class InsititialAdJavaClass {
    var mInterstitialAd: InterstitialAd? = null
    var dialog1: Dialog?=null

    fun insititial_load_show(activity: Activity) {
        insititailAdLoad(activity, 1)
    }

    @SuppressLint("SuspiciousIndentation")
    fun insititailAdShow(activity: Activity) {

        //int counter= Integer.parseInt(s.remoteValue("counter",activity));
        if (CounterJavaClass.counter == 0) {
            CounterJavaClass.counter = 3
        }
        CounterJavaClass.count = CounterJavaClass.count + 1
        Log.i("shownativead", "counter=: "+count+"and"+counter)
//        Toast.makeText(
//            activity,
//            "count" + CounterJavaClass.count % CounterJavaClass.counter + "=and=",
//            Toast.LENGTH_SHORT
//        ).show()
        if (mInterstitialAd != null && CounterJavaClass.count % CounterJavaClass.counter == 0) {


            mInterstitialAd?.show(activity)
            insitiailisshowing = true
            /*dialog1 = Dialog(activity)

                dialog1?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog1?.setCancelable(false)
                dialog1?.setContentView(R.layout.layout_dialog)
                dialog1?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                // Adjust dialog window attributes to keep it centered
                val window = dialog1?.window
                val layoutParams = window?.attributes
                layoutParams?.gravity = Gravity.CENTER
                window?.attributes = layoutParams

                dialog1?.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    dialog1?.dismiss()

                }, 3000)
*/


//
//
//            val dialog = Dialog(activity)
//            dialog.setContentView(R.layout.layout_dialog)
//            // dialog.setCancelable(true);
//            dialog.show()
//            Log.i("shownativead", "in")
//            val handler = Handler()
//            handler.postDelayed({ // Call your method here
//                if (dialog != null && dialog.isShowing) {
//                    dialog.dismiss()
//                    Log.i("shownativead", "show")
//                    mInterstitialAd!!.show(activity)
//                    insitiailisshowing = true
//                }
//            }, 5000) // Delay of 1 second (1000 milliseconds)
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    Log.i("shownativead", "counter=: "+count+"and"+counter)
                    // Called when a click is recorded for an ad.
                    Log.d(ContentValues.TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                    mInterstitialAd = null
                    insitiailisshowing = false
                    insititailAdLoad(activity, 1)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when ad fails to show.
                    Log.e(ContentValues.TAG, "Ad failed to show fullscreen content.")
                    mInterstitialAd = null
                    Log.i("shownativead", "failed to show")
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(ContentValues.TAG, "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    //  Toast.makeText(activity, "show hogya", Toast.LENGTH_SHORT).show();
                    // Called when ad is shown.
                    Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                }
            }
            // Toast.makeText(activity, "show below", Toast.LENGTH_SHORT).show();
        } else {
            if (mInterstitialAd == null && CounterJavaClass.count % CounterJavaClass.counter == 0) {
                insititailAdLoad(activity, 1)
                // Toast.makeText(activity, "again load", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(activity, "else", Toast.LENGTH_SHORT).show();
        }
    }

    fun insititailAdLoad(activity: Activity, status: Int) {
//        val s = SharedPrefRemote()
//        var adidR = s.remoteValue("interstitialId", activity)
//        if (adidR == "") {
//            adidR = activity.getString(R.string.interstitial_ad_id)
//
//            // Toast.makeText(activity, " if adid"+adidR, Toast.LENGTH_SHORT).show();
//            Log.i("shownativead", "insitital ad if: success fully" + adidR + "and status" + status)
//        }
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, activity.getString(R.string.incorrectEmail), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                Log.i("shownativead", "loaded insiticial")
                mInterstitialAd = interstitialAd
                //Toast.makeText(activity, "loaded", Toast.LENGTH_SHORT).show();
                // mInterstitialAd.show(activity);
                Log.i(ContentValues.TAG, "onAdLoaded")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // Handle the error
                Log.i("shownativead", "not loaded insititail")
                //Toast.makeText(activity, "first not loaded ", Toast.LENGTH_SHORT).show();
                Log.d(ContentValues.TAG, loadAdError.toString())
                mInterstitialAd = null
            }
        })
    }

    companion object {
        //count from 0 to 3 + incriment and counter is finsh line like count % counter =0
        //SharedPrefRemote s=new SharedPrefRemote();
        @JvmField
        var insitiailisshowing = false
    }
}