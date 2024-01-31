package com.mk.diary.presentation.ui.settings

import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mk.diary.presentation.ui.boardpassing.BoardPassActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.shortToast
import com.mk.diary.utils.companion.Static
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentPasswordSettingBinding


class PasswordSettingFragment : Fragment() {
    var check = false
    lateinit var binding: FragmentPasswordSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPasswordSettingBinding.inflate(inflater,container,false)
        check = false
        handleTurnOffButton()
        clickButton()
      //  Log.d("notifyValue", "button: "+SharedPrefObj.getBoolean(requireContext(), MyConstants.SKIP_TOKEN,false))
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.fingerprintbutton.setOnCheckedChangeListener { _, isChecked ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Fingerprint API only available from Android 6.0 (M)
                val fingerprintManager =
                    requireContext().getSystemService(FingerprintManager::class.java)

                if (fingerprintManager == null) {
                    // Fingerprint sensor is not available
                    disableFingerprintButton()
                } else if (!fingerprintManager.isHardwareDetected) {
                    // Fingerprint sensor is not available
                    disableFingerprintButton()
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // No fingerprints are enrolled
                    disableFingerprintButton()
                } else {
                    // Everything is ready for fingerprint authentication
                    enableFingerprintButton(isChecked)
                }
            }
        }
        if (SharedPrefObj.getBoolean(requireContext(), MyConstants.FINGERPRINT_TOKEN, false)) {
            binding.fingerprintbutton.isChecked = true
        }
        return binding.root
    }
    private fun clickButton(){
        binding.disableButton.setOnClickListener {
           /* if (!check){*/
                if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                    SharedPrefObj.saveBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)
                    binding.disableButton.visibility=View.GONE
                    SharedPrefObj.removePasswordList(requireContext())
                    SharedPrefObj.removeEmail(requireContext())
                    binding.paswordChangeBtn.setText(resources.getString(R.string.pin))
                }else{
                    binding.disableButton.visibility=View.GONE
                    SharedPrefObj.removeEmail(requireContext())
                    binding.paswordChangeBtn.setText(resources.getString(R.string.pin))
                    SharedPrefObj.removePasswordList(requireContext())
                    SharedPrefObj.saveBoolean(requireContext(),MyConstants.SKIP_TOKEN,true)
                }
       /*         check = true
            }else{
                if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                    SharedPrefObj.saveBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)
                    binding.disableButton.setText("Turn Off Passcode")
                }else{
                    SharedPrefObj.saveBoolean(requireContext(),MyConstants.SKIP_TOKEN,true)
                    binding.disableButton.setText("Turn On Passcode")
                }
                check = false
            }*/
        }
    }
    private fun handleTurnOffButton(){
        if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
            binding.disableButton.setText("Turn On Passcode")
            binding.disableButton.visibility=View.GONE
            binding.paswordChangeBtn.setText(resources.getString(R.string.pin))
            binding.paswordChangeBtn.setTextColor(resources.getColor(R.color.unselected_State_Color))
        }else{
            binding.disableButton.visibility=View.VISIBLE
            binding.disableButton.setText("Turn Off Passcode")
            binding.paswordChangeBtn.setText("Change Passcode")
            binding.paswordChangeBtn.setTextColor(resources.getColor(R.color.color43))
        }
        binding.paswordChangeBtn.setOnClickListener {
            if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
               /* if (SharedPrefObj.getPasswordList(requireContext())?.isNotEmpty()==true
                    || SharedPrefObj.getEmail(requireContext())!=null){
                    findNavController().navigate(R.id.action_passwordSettingFragment_to_passwordResetFragment)
                }else{*/
                    val i = Intent(requireContext(), BoardPassActivity::class.java)
                    i.putExtra(MyConstants.PASS_DATA, "P")
                    Static.passwordReset = "Reset"
                    Static.PASSWORD_CHANGE = true
                    startActivity(i)
             /*   }*/
            }else{
                Static.passwordReset = "Reset"
                Static.PASSWORD_CHANGE = true
                findNavController().navigate(R.id.action_passwordSettingFragment_to_passwordResetFragment)
            }
        }
    }
    private fun disableFingerprintButton() {
        binding.fingerprintbutton.isChecked = false
        binding.fingerprintbutton.isClickable = false // Disable the button
        binding.fingerprintbutton.setOnClickListener {
            showSnackbar("Fingerprint sensor not available.")
        }
    }

    private fun enableFingerprintButton(isChecked: Boolean) {
        binding.fingerprintbutton.isClickable = true
        if (isChecked) {
            // Perform the button click action here (e.g., start fingerprint authentication)
            SharedPrefObj.saveBoolean(requireContext(), MyConstants.FINGERPRINT_TOKEN, true)
        } else {
            SharedPrefObj.saveBoolean(requireContext(), MyConstants.FINGERPRINT_TOKEN, false)
        }
    }
    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackbar.setAction("Dismiss") {
            // Handle action button click if needed
            snackbar.dismiss()
        }
        snackbar.show()
    }

}