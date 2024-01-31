package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentPasswordBinding
import java.util.concurrent.Executor

@AndroidEntryPoint
class PasswordFragment : Fragment() {
    var checkUserEntery = false
    private lateinit var fingerprintManager: FingerprintManagerCompat
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var binding: FragmentPasswordBinding
    private val listPassword: ArrayList<String> by lazy { ArrayList() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPasswordBinding.inflate(inflater, container, false)
        fingerprintManager = FingerprintManagerCompat.from(requireContext())
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = createBiometricPrompt()
        promptInfo = createBiometricPromptInfo()
        if (SharedPrefObj.getAppTheme(requireContext()) != null) {
            val model = SharedPrefObj.getAppTheme(requireContext())
            Glide.with(requireContext()).load(model.theme)
                .into(binding.backGroundTheme)
        }
        if (SharedPrefObj.getPasswordList(requireContext()) != null) {
            binding.forgotPasswordBtn.visibility = View.VISIBLE
        } else {
            binding.forgotPasswordBtn.visibility = View.GONE
        }
        binding.forgotPasswordBtn.setOnClickListener {
            findNavController().navigate(R.id.action_passwordFragment_to_passwordVerificationFragment)
        }
        // Set onClickListener for each button

        // Set onClickListener for back button
        val checkTrue=arguments?.getString(MyConstants.PASS_DATA)
        val passwordList = SharedPrefObj.getPasswordList(requireContext())
        val email = SharedPrefObj.getEmail(requireContext())
        val checkTrueValue = checkTrue
        if (passwordList==null && email !=null && Static.passwordReset==null
            && checkTrueValue==null){

            forgotPasswordAlert()
        }
        if (SharedPrefObj.getBoolean(requireContext(), MyConstants.FINGERPRINT_TOKEN, false)) {
            if (Static.passwordReset == "Reset") {
            } else {
                initiateFingerprintAuthentication()
            }
        }else{

        }
        binding.backMinosBtn.setOnClickListener {
            handleBackButtonClick()
            updateUi()
        }
        buttonClicks()
        return binding.root
    }
    private fun buttonClicks(){
        binding.oneBtn.setOnClickListener {
            addDigit("1")
        }
        binding.twoBtn.setOnClickListener {
            addDigit("2")
        }
        binding.threeBtn.setOnClickListener {
            addDigit("3")
        }
        binding.fourBtn.setOnClickListener {
            addDigit("4")
        }
        binding.fiveBtn.setOnClickListener {
            addDigit("5")
        }
        binding.sixBtn.setOnClickListener {
            addDigit("6")
        }
        binding.sevenBtn.setOnClickListener {
            addDigit("7")
        }
        binding.eightBtn.setOnClickListener {
            addDigit("8")
        }
        binding.nineBtn.setOnClickListener {
            addDigit("9")
        }
        binding.zeroBtn.setOnClickListener {
            addDigit("0")
        }
    }
    private fun addDigit(password: String) {
        listPassword.add(password)
        updateUi()
        passwordCheck()
    }
    private fun updateUi() {
        binding.passCodeOne.setImageResource(R.drawable.black_circle_sh)
        binding.passCodeTwo.setImageResource(R.drawable.black_circle_sh)
        binding.passCodeThree.setImageResource(R.drawable.black_circle_sh)
        binding.passCodeFour.setImageResource(R.drawable.black_circle_sh)
        when(listPassword.size){
            0->{
                binding.passCodeOne.setImageResource(R.drawable.black_circle_sh)
                binding.passCodeTwo.setImageResource(R.drawable.black_circle_sh)
                binding.passCodeThree.setImageResource(R.drawable.black_circle_sh)
                binding.passCodeFour.setImageResource(R.drawable.black_circle_sh)
            }
            1->{
                binding.passCodeOne.setImageResource(R.drawable.black_cir_sh)

            }
            2->{
                binding.passCodeOne.setImageResource(R.drawable.black_cir_sh)
                binding.passCodeTwo.setImageResource(R.drawable.black_cir_sh)
            }
            3->{
                binding.passCodeOne.setImageResource(R.drawable.black_cir_sh)
                binding.passCodeTwo.setImageResource(R.drawable.black_cir_sh)
                binding.passCodeThree.setImageResource(R.drawable.black_cir_sh)
            }
            4->{
                binding.passCodeOne.setImageResource(R.drawable.black_cir_sh)
                binding.passCodeTwo.setImageResource(R.drawable.black_cir_sh)
                binding.passCodeThree.setImageResource(R.drawable.black_cir_sh)
                binding.passCodeFour.setImageResource(R.drawable.black_cir_sh)
            }
        }
    }

    private fun forgotPasswordAlert(){
        binding.customAlert.visibility=View.VISIBLE
        binding.setpaswordBtn.setOnClickListener {
            binding.customAlert.visibility=View.GONE
            findNavController().navigate(
                R.id.action_passwordFragment_to_passwordVerificationFragment,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (listPassword.isNotEmpty()){
            listPassword.clear()
        }
    }
    private fun passwordCheck() {
        if (listPassword.isNotEmpty()){
            if (listPassword.size== 4) {
                navigateToNewScreen(listPassword)

            } else if (listPassword.size>4){
                listPassword.clear()
                showSnackbar(resources.getString(R.string.maxsizePassword))
            }
        }
    }
    private fun createBiometricPrompt(): BiometricPrompt {
        return BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Handle authentication error
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                requireContext().newScreen(BottomNavActivity::class.java)
                // Authentication succeeded, handle it accordingly
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Authentication failed
            }
        })
    }
    private fun createBiometricPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
    }
    private fun initiateFingerprintAuthentication() {
        biometricPrompt.authenticate(promptInfo)
    }
    private fun handleBackButtonClick() {
        if (listPassword.isNotEmpty()) {
            listPassword.removeAt(listPassword.size - 1)
            updateUi()
        } else {
            showSnackbar(resources.getString(R.string.nodigitstoremove))
        }
    }

    private fun navigateToNewScreen(listOFPassword: ArrayList<String>) {
        if (SharedPrefObj.getEmail(requireContext()) != null && SharedPrefObj.getPasswordList(
                requireContext()
            ) != null
        ) {
            val enteredPassword = listOFPassword.joinToString(separator = "")
            if (enteredPassword == SharedPrefObj.getPasswordList(requireContext())
                    ?.joinToString(separator = "")
            ) {
                if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                    val bundle = Bundle()
                    bundle.putStringArrayList(MyConstants.PASS_DATA, listOFPassword)
                    findNavController().navigate(
                        R.id.action_passwordFragment_to_setPasswordFragment,
                        bundle
                    )
                    checkUserEntery = true
                }else if (Static.PASSWORD_CHANGE){

                }else{
                    requireContext().newScreen(BottomNavActivity::class.java)
                    checkUserEntery = true
                }
                // Passwords match, do something (e.g., navigate to a success screen)
            } else {
                if (Static.PASSWORD_CHANGE){
                    val bundle = Bundle()
                    bundle.putStringArrayList(MyConstants.PASS_DATA, listOFPassword)
                    findNavController().navigate(
                        R.id.action_passwordFragment_to_setPasswordFragment,
                        bundle
                    )
                }else{
                    updateUi()
                    listOFPassword.clear()
                    showSnackbar("Incorrect password. Please try again.")
                }
            }
        } else {
            val bundle = Bundle()
            bundle.putStringArrayList(MyConstants.PASS_DATA, listOFPassword)
            findNavController().navigate(
                R.id.action_passwordFragment_to_setPasswordFragment,
                bundle
            )
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