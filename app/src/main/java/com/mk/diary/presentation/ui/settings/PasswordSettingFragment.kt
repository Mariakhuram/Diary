package com.mk.diary.presentation.ui.settings

import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentPasswordSettingBinding


class PasswordSettingFragment : Fragment() {

    lateinit var binding: FragmentPasswordSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPasswordSettingBinding.inflate(inflater,container,false)
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.paswordChangeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_passwordSettingFragment_to_passwordResetFragment)
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