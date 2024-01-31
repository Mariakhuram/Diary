package com.mk.diary.presentation.splash

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.mk.diary.presentation.ui.boardpassing.BoardPassActivity
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentSplashBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.mk.diary.AdsImplimentation.AppOpenAdX
import com.mk.diary.AdsImplimentation.NewInsitisialAd
import com.mk.diary.AdsImplimentation.printIt
import com.mk.diary.utils.appext.shortToast
import java.nio.channels.NotYetBoundException
import java.util.Date

@AndroidEntryPoint
class SplashFragment : Fragment() {
    var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest
    private var progressStatus = 0
    private val handler = Handler()
    lateinit var binding: FragmentSplashBinding
    lateinit var appOpenAd: AppOpenAd
    private var loadTime: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSplashBinding.inflate(layoutInflater,container,false)
     //   loadAd(requireContext())
        val p=  SharedPrefObj.getPasswordList(requireContext())
        val e=SharedPrefObj.getEmail(requireContext())
        Log.d("notifyValue", "splash: ${p}  and ${e}")

        Log.d("notifyValue", "splash: "+SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false))

        if (Static.checkLangBoolean){
            progressBar()
        }
        return binding.root
    }
    fun loadAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        try {
            // Log.d("interstital id",RemoteConfigs.interstitial_ad_id)
            InterstitialAd.load(
                context, context.getString(R.string.admobInterstitialAd), adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        mInterstitialAd = null
                        loadAd(context)

                    }
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        mInterstitialAd = interstitialAd
                        Log.d("cvv", "onAdLoaded: ")
                    }
                }
            )
        } catch (e: Exception) {


        }

    }
    private fun progressBar() {
        SharedPrefObj.saveBoolean(requireContext(), MyConstants.KEY_HAS_SEEN_BOTTOM_SHEET,false)
        val startTime = System.currentTimeMillis()
        val duration = 4000L // 7 seconds in milliseconds
        val incrementInterval = 50L // Adjust the interval for a smoother progress

        Thread {
            while (System.currentTimeMillis() - startTime < duration) {
                // Calculate the progress based on elapsed time
                val elapsedTime = System.currentTimeMillis() - startTime
                progressStatus = (elapsedTime * 100 / duration).toInt()

                // Update the progress bar and display the current value in the text view
                handler.post {
                    binding.progressBar.progress = progressStatus
                    binding.progressPercentageTv.text = "$progressStatus%"
                }

                try {
                    // Sleep for the specified interval
                    Thread.sleep(incrementInterval)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            // Ensure that progressStatus is set to 100 at the end
            handler.post {
                binding.progressBar.progress = 100
                binding.progressPercentageTv.text = "100%"
            }

            lifecycleScope.launchWhenResumed {
           /*     if (mInterstitialAd!=null){
                    binding.showAdDialogue.visibility=View.VISIBLE
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        binding.showAdDialogue.visibility=View.GONE
                        mInterstitialAd!!.show(requireActivity())
                        mInterstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    Log.d(NewInsitisialAd.TAG, "onAdDismissedFullScreenContent: ")
                                    // Don't forget to set the ad reference to null so you
                                    // don't show the ad a second time.
                                    mInterstitialAd = null
                                    loadAd(requireContext())*/
                val token= SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)
                val password=SharedPrefObj.getPasswordList(requireContext())
                val email=SharedPrefObj.getEmail(requireContext())
                val firstTimeToken=SharedPrefObj.getToken(requireContext())
                if (firstTimeToken.equals("USER_TOEKN_SAVED")){
                    if (token){
                        Log.d("notifyValue", "splase token availble,sp print${token}")
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }else{
                        Log.d("notifyValue", "splase token availble,sp print${token}")
                        val i = Intent(requireContext(), BoardPassActivity::class.java)
                        i.putExtra(MyConstants.PASS_DATA, "P")
                        startActivity(i)
                    }
                }else{
                    Log.d("notifyValue", "splase not token availble")
                    findNavController().navigate(R.id.selectLanguageFragment2)
                }
              /*  if (!token && password?.isNotEmpty()==true && email?.isNotEmpty()==true){
                    val i = Intent(requireContext(), BoardPassActivity::class.java)
                    i.putExtra(MyConstants.PASS_DATA, "P")
                    startActivity(i)
                    Log.d("notifyValue", "splash: skip falls, pss available, go passcode")
                }

                if (token){
                    Log.d("notifyValue", "splas token true ")
                    requireContext().newScreen(BottomNavActivity::class.java)
                }else{
                    findNavController().navigate(R.id.selectLanguageFragment2)
                }*/
                /*if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                    requireContext().newScreen(BottomNavActivity::class.java)
                    Log.d("notifyValue", "splase skip token true")
                }else{
                    if (SharedPrefObj.getPasswordList(requireContext())?.isNotEmpty()==true){
                        if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                            requireContext().newScreen(BottomNavActivity::class.java)
                            Log.d("notifyValue", "splase skip token true and password is not empty")
                        }else{
                            Log.d("notifyValue", "splase skip token false and password is not empty" +
                                    "${SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)}")
                            requireContext().newScreen(BottomNavActivity::class.java)
                        }
                    }else{
                        Log.d("notifyValue", "splase skip token false and empty passsword  ")
                        findNavController().navigate(R.id.selectLanguageFragment2)
                    }
                }*/
/*
                                    if (SharedPrefObj.getPasswordList(requireContext())?.isNotEmpty()==true) {
                                        if (!SharedPrefObj.getBoolean(requireContext(), MyConstants.SKIP_TOKEN, false)) {
                                            requireContext().newScreen(BottomNavActivity::class.java)
                                            Log.d("notifyValue", "splash: skip true, pss available")
                                        } else {
                                            val i = Intent(requireContext(), BoardPassActivity::class.java)
                                            i.putExtra(MyConstants.PASS_DATA, "P")
                                            startActivity(i)
                                            Log.d("notifyValue", "splash: skip falls, pss available")
                                        }
                                    } else {
                                        if (SharedPrefObj.getBoolean(requireContext(), MyConstants.SKIP_TOKEN, false)) {
                                            requireContext().newScreen(BottomNavActivity::class.java)
                                            Log.d("notifyValue", "splash: skip true, pss not")
                                        } else {
                                            Log.d("notifyValue", "splash: skip falss, pss not")
                                            findNavController().navigate(R.id.selectLanguageFragment2)
                                        }
                                    }
*/
/*
                                }
                                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                    Log.d(NewInsitisialAd.TAG, "onAdFailedToShowFullScreenContent: ")
                                    // Don't forget to set the ad reference to null so you
                                    // don't show the ad a second time.
                                    mInterstitialAd = null
                                }
                                override fun onAdShowedFullScreenContent() {
                                    Log.d(NewInsitisialAd.TAG, "onAdShowedFullScreenContent: ")
                                    // Called when ad is dismissed.
                                }
                            }
                    }*/
      /*      }else{*/
                  /*  if (SharedPrefObj.getToken(requireContext()) != null) {
                        if (SharedPrefObj.getBoolean(requireContext(), MyConstants.SKIP_TOKEN, false)) {
                            requireContext().newScreen(BottomNavActivity::class.java)
                        } else {
                            val i = Intent(requireContext(), BoardPassActivity::class.java)
                            i.putExtra(MyConstants.PASS_DATA, "P")
                            startActivity(i)
                        }
                    } else {
                        if (SharedPrefObj.getBoolean(requireContext(), MyConstants.SKIP_TOKEN, false)) {
                            requireContext().newScreen(BottomNavActivity::class.java)
                        } else {
                            findNavController().navigate(R.id.selectLanguageFragment2)
                        }
                    }
                }*/
            }
        }.start()
    }
}