package com.mk.diary.presentation.ui.settings


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mk.diary.adapters.recyclerview.AppThemeRecAdapter
import com.mk.diary.helpers.ResultCase
import com.mk.diary.presentation.ui.navigation.MainActivity
import com.mk.diary.presentation.viewmodels.theme.BackgroundThemeViewModel
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.shortToast
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentMenuSettingsBinding


@AndroidEntryPoint
class MenuSettingsFragment : Fragment() {
    private val viewModel :BackgroundThemeViewModel by viewModels()
    lateinit var binding: FragmentMenuSettingsBinding
    private val modelSList :ArrayList<String> by lazy { ArrayList() }
    private val mAdapter by lazy { AppThemeRecAdapter(modelSList) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMenuSettingsBinding.inflate(inflater,container,false)
        viewModel.getSlidingTheme()
        backUpBtn()
/*
        binding.backGoogleDriveBtn.setOnClickListener {
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            if (account == null) {
                requestUserSignIn()
            } else {
                val credential = GoogleAccountCredential.usingOAuth2(
                    requireContext(),
                    setOf(DriveScopes.DRIVE_FILE)
                )
                credential.selectedAccount = account.account

                val googleDriveService = com.google.api.services.drive.Drive.Builder(
                    AndroidHttp.newCompatibleTransport(),
                    GsonFactory(),
                    credential
                )
                    .setApplicationName("AppName")
                    .build()

                mDriveServiceHelper = DriveServiceHelper(googleDriveService)
            }

        }
*/
        binding.deleteCardViewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_menuSettingsFragment_to_deleteDatabaseFragment)
        }
        binding.cardTheme.setOnClickListener {
            findNavController().navigate(R.id.action_menuSettingsFragment_to_appThemeFragment)
        }
        rateUsBtn()
        passwordReset()
        localization()
        viewModel()
        handleNotifications()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        binding.rateUsLayoutBtn.visibility=View.GONE
    }
    private fun rateUsBtn() {
        binding.privacypolicytxt.setOnClickListener {
            val url = "https://sites.google.com/view/audiodiary/home"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        binding.rateusimage.setOnClickListener {
            binding.rateUsLayoutBtn.visibility=View.VISIBLE
        }

        binding.rateUSLayout.button.setOnClickListener {
            binding.rateUsLayoutBtn.visibility=View.GONE
        }
        binding.feedBackBtn.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                val email = "mehtabali40console@gmail.com" // replace with the desired email address
                val subject = ""
                val body = ""
                val data = Uri.parse("mailto:$email?subject=$subject&body=$body")
                intent.data = data
                startActivity(intent)
            }catch (e:Exception){
            }
        }
        binding.rateUSLayout.star1.setOnClickListener {
            binding.rateUSLayout.star1.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star2.setImageResource(R.drawable.ratesunselectedicone)
            binding.rateUSLayout.star3.setImageResource(R.drawable.ratesunselectedicone)
            binding.rateUSLayout.star4.setImageResource(R.drawable.ratesunselectedicone)
            binding.rateUSLayout.star5.setImageResource(R.drawable.ratesunselectedicone)
            checkStars(condition1 = true, condition2 = false)
        }
        binding.rateUSLayout.star2.setOnClickListener {
            binding.rateUSLayout.star1.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star2.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star3.setImageResource(R.drawable.ratesunselectedicone)
            binding.rateUSLayout.star4.setImageResource(R.drawable.ratesunselectedicone)
            binding.rateUSLayout.star5.setImageResource(R.drawable.ratesunselectedicone)
            checkStars(condition1 = true, condition2 = false)

        }
        binding.rateUSLayout.star3.setOnClickListener {
            binding.rateUSLayout.star1.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star2.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star3.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star4.setImageResource(R.drawable.ratesunselectedicone)
            binding.rateUSLayout.star5.setImageResource(R.drawable.ratesunselectedicone)
            checkStars(condition1 = true, condition2 = false)
        }
        binding.rateUSLayout.star4.setOnClickListener {
            binding.rateUSLayout.star1.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star2.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star3.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star4.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star5.setImageResource(R.drawable.ratesunselectedicone)
            checkStars(condition1 = false, condition2 = true)
        }
        binding.rateUSLayout.star5.setOnClickListener {
            binding.rateUSLayout.star1.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star2.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star3.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star4.setImageResource(R.drawable.ratesselectedicone)
            binding.rateUSLayout.star5.setImageResource(R.drawable.ratesselectedicone)
            checkStars(condition1 = false, condition2 = true)
        }
    }
    private fun checkStars(condition1: Boolean, condition2: Boolean) {
        if (condition1) {
            binding.rateUSLayout.btnratenow.setOnClickListener {
                try {
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.data = Uri.parse("mailto:mehtabali40console@gmail.com")
                    startActivity(emailIntent)
                }catch (e:Exception){
                }
            }
        } else if (condition2) {
            binding.rateUSLayout.btnratenow.setOnClickListener {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + requireContext().packageName)
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().packageName)                        ))
                }
            }
        }
    }
    private fun viewModel(){
        binding.appThemeRec.adapter=mAdapter
        viewModel.sliderThemeLiveData.observe(viewLifecycleOwner){result->
            when(result){
                is ResultCase.Loading->{
                }
                is ResultCase.Success->{
                    if (result.data.isNotEmpty()){
                        modelSList.clear()
                        modelSList.addAll(result.data)
                        mAdapter.notifyDataSetChanged()
                    }
                }
                is ResultCase.Error->{
                    requireContext().shortToast(result.message.toString())
                }

                else -> {}
            }
        }
        mAdapter.recyclerClick(object:AppThemeRecAdapter.PassRateData{
            override fun clickFunction(modelClass: String, position: Int) {
                val b=Bundle()
                b.putInt(MyConstants.PASS_DATA,position)
                findNavController().navigate(R.id.action_menuSettingsFragment_to_appThemeFragment,b)
            }
        })
    }

    private fun passwordReset() {
        binding.lockpasscodeicone.setOnClickListener {
            findNavController().navigate(R.id.action_menuSettingsFragment_to_passwordSettingFragment)
        }
    }

    private fun localization() {
        binding.laguagetxt.setOnClickListener {
            Static.checkLangBoolean = false
            val i= Intent(requireContext(), MainActivity::class.java)
            i.putExtra(MyConstants.PASS_DATA,"SetLan")
            Static.settLang="SetLan"
            startActivity(i)
        }
    }
    private fun handleNotifications() {
        // Check if this is the first time the user is visiting the app
       if (SharedPrefObj.getBoolean(requireContext(), MyConstants.NOTIFICATION_CLICK,false)){
//           requireContext().shortToast("Notifications: will not come")
           binding.notificationsBtn.isChecked = false
       }else{
//           requireContext().shortToast("Notifications: will  come")
           binding.notificationsBtn.isChecked = true
       }

        binding.notificationsBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                SharedPrefObj.saveBoolean(requireContext(), MyConstants.NOTIFICATION_CLICK,false)
            } else {
                SharedPrefObj.saveBoolean(requireContext(), MyConstants.NOTIFICATION_CLICK,true)
            }
        }
    }
    private fun backUpBtn() {
        binding.backGoogleDriveBtn.setOnClickListener {
        }
        val theme= SharedPrefObj.getAppTheme(requireContext())
        binding.backGoogleDriveBtn.setCardBackgroundColor(theme.color)
        // Change track color
    }
}