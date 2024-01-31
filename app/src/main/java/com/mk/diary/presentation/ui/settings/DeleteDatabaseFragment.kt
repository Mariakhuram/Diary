package com.mk.diary.presentation.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mk.diary.AdsImplimentation.NewInsitisialAd
import com.mk.diary.localization.SharedPref
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.presentation.viewmodels.noteview.NoteViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.appext.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentDeleteDatabaseBinding

@AndroidEntryPoint
class DeleteDatabaseFragment : Fragment() {
    var mInterstitialAd: InterstitialAd? = null
    private val viewModel :NoteViewModel by viewModels()
    lateinit var binding: FragmentDeleteDatabaseBinding
    var deleteDataType:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentDeleteDatabaseBinding.inflate(inflater,container,false)
        loadAd(requireContext())
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        deleteDataType = "Local"
        deleteData()
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
    private fun deleteData() {
        binding.localRadioBtn.setOnClickListener {
            deleteDataType="Local"
            binding.localRadioBtn.setImageResource(R.drawable.radio_button_checked)
            binding.cloudRadioBtn.setImageResource(R.drawable.radio_button_unchecked)
        }
        binding.cloudRadioBtn.setOnClickListener {
            deleteDataType="Cloud"
            binding.cloudRadioBtn.setImageResource(R.drawable.radio_button_checked)
            binding.localRadioBtn.setImageResource(R.drawable.radio_button_unchecked)
        }
        binding.btndelete.setOnClickListener {
            deleteDataType?.let { it1 -> deleteBottomSheet(it1) }

        }
        if (deleteDataType=="Local"){
            binding.localRadioBtn.setImageResource(R.drawable.radio_button_checked)
            binding.cloudRadioBtn.setImageResource(R.drawable.radio_button_unchecked)
        }
    }
    private fun deleteBottomSheet(modelClass: String) {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.delete_bottom_sheet, null)
        val cancelBtn = view.findViewById<TextView>(R.id.cancelBtn)
        val deleteBtn = view.findViewById<TextView>(R.id.deleteBtn)
        val imageRec = view.findViewById<ImageView>(R.id.mainImage)
        val mainDesc = view.findViewById<TextView>(R.id.mainDesc)
        val mainTitle = view.findViewById<TextView>(R.id.mainTitle)
        cancelBtn.setText(resources.getString(R.string.discard))
        mainTitle.setText(resources.getString(R.string.warning))
        mainDesc.setText(resources.getString(R.string.warningerror))
        imageRec.setImageResource(R.drawable.errorwarningicon)

        // Connect TabLayout and ViewPager2

        deleteBtn.setOnClickListener {
            deleteBtn.isClickable = false
           /* if (mInterstitialAd != null) {
                binding.showAdDialogue.visibility=View.VISIBLE
                Log.i("shownativead", "ini= show dia")
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
                                loadAd(requireContext())
                                    try {
                                        viewModel.deleteAllData()
                                        requireContext().newScreen(BottomNavActivity::class.java)
                                    }catch (e:Exception){
                                        requireContext().shortToast(e.message.toString())
                                    }

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
                }
            }else{*/
                try{
                    viewModel.deleteAllData()
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }catch (e:Exception){
                        requireContext().shortToast(e.message.toString())
                    }

         /*   }*/
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()
    }


}