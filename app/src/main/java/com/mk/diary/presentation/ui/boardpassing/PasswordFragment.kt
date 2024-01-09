package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    private val listOFPassword: ArrayList<String> by lazy { ArrayList() }
    lateinit var binding: FragmentPasswordBinding
    private var maxImages = 4
    private val maxDigits = 10
    private val listPassword: ArrayList<String> by lazy { ArrayList() }
    private val imageList = ArrayList<ImageView>()
    private val imageViewList = ArrayList<ImageView>()
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
        imageViewList.addAll(
            listOf(
                binding.passCodeOne,
                binding.passCodeTwo,
                binding.passCodeThree,
                binding.passCodeFour
            )
        )
        // Dynamically create ImageViews
        for (i in 0 until maxImages) {
            val imageView = ImageView(requireContext())
            imageView.setImageResource(R.drawable.black_circle_sh)
            imageViewList.add(imageView)
            binding.imageViewContainer.addView(imageView)
        }
        // Set onClickListener for each button
        setButtonClickListeners()
        // Set onClickListener for back button
        binding.backMinosBtn.setOnClickListener {
            handleBackButtonClick()
        }
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
        return binding.root
    }
    private fun forgotPasswordAlert(){
        binding.customAlert.visibility=View.VISIBLE
        binding.forgotPasswordLayout.setpaswordBtn.setOnClickListener {
            binding.customAlert.visibility=View.GONE
            findNavController().navigate(
                R.id.action_passwordFragment_to_passwordVerificationFragment,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkUserEntery){
            binding.imageViewContainer.visibility=View.INVISIBLE
            binding.imageViewContainer2.visibility=View.VISIBLE
            changePassword()
        }
    }
    private fun changePassword(){
        imageList.addAll(
            listOf(
                binding.passOne,
                binding.passTwo,
                binding.passThree,
                binding.passFour
            )
        )
        // Dynamically create ImageViews
        for (i in 0 until maxImages) {
            val imageView = ImageView(requireContext())
            imageView.setImageResource(R.drawable.black_circle_sh)
            imageList.add(imageView)
            binding.imageViewContainer2.addView(imageView)
        }

        // Set onClickListener for each button
        setClickListeners()

        // Set onClickListener for back button
        binding.backMinosBtn.setOnClickListener {
            handleBackClick()
        }
    }
    private fun setClickListeners() {
        val buttonArray = arrayOf(
            binding.oneBtn,
            binding.twoBtn,
            binding.threeBtn,
            binding.fourBtn,
            binding.fiveBtn,
            binding.sixBtn,
            binding.sevenBtn,
            binding.eightBtn,
            binding.nineBtn,
            binding.zeroBtn
        )

        for (i in 0 until maxDigits) {
            buttonArray[i].setOnClickListener {
                handleClick(i + 1)
            }
        }
    }
    private fun handleClick(value: Int) {
        if (listPassword.size < maxImages) {
            // Adjust value to match the button text (1 to 9, 0)
            val adjustedValue = if (value == 10) "0" else value.toString()

            listPassword.add(adjustedValue)

            val resId = R.drawable.black_cir_sh
            imageList[listPassword.size - 1].setImageResource(resId)

            if (listPassword.size == maxImages) {
                // Password is complete, navigate to the new screen
                navigateToNewScreen(listPassword)
            }
        } else {
            showSnackbar(resources.getString(R.string.maxsizePassword))
        }
    }
    private fun handleBackClick() {
        if (listPassword.isNotEmpty()) {
            listPassword.removeAt(listPassword.size - 1)
            val lastEnteredIndex = listPassword.size
            imageList[lastEnteredIndex].setImageResource(R.drawable.black_circle_sh)
        } else {
            showSnackbar(resources.getString(R.string.nodigitstoremove))
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

    private fun setButtonClickListeners() {
        val buttonArray = arrayOf(
            binding.oneBtn,
            binding.twoBtn,
            binding.threeBtn,
            binding.fourBtn,
            binding.fiveBtn,
            binding.sixBtn,
            binding.sevenBtn,
            binding.eightBtn,
            binding.nineBtn,
            binding.zeroBtn
        )

        for (i in 0 until maxDigits) {
            buttonArray[i].setOnClickListener {
                handleButtonClick(i + 1)
            }
        }
    }

    private fun handleButtonClick(value: Int) {
        if (listOFPassword.size < maxImages) {
            // Adjust value to match the button text (1 to 9, 0)
            val adjustedValue = if (value == 10) "0" else value.toString()

            listOFPassword.add(adjustedValue)

            val resId = R.drawable.black_cir_sh
            imageViewList[listOFPassword.size - 1].setImageResource(resId)

            if (listOFPassword.size == maxImages) {
                // Password is complete, navigate to the new screen
                navigateToNewScreen(listOFPassword)
            }
        } else {
            showSnackbar(resources.getString(R.string.maxsizePassword))
        }
    }

    private fun handleBackButtonClick() {
        if (listOFPassword.isNotEmpty()) {
            listOFPassword.removeAt(listOFPassword.size - 1)

            val lastEnteredIndex = listOFPassword.size
            imageViewList[lastEnteredIndex].setImageResource(R.drawable.black_circle_sh)
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
                // Passwords match, do something (e.g., navigate to a success screen)
                requireContext().newScreen(BottomNavActivity::class.java)
                checkUserEntery = true
            } else {
                showSnackbar("Incorrect password. Please try again.")
            }
        } else {
            val bundle = Bundle()
            bundle.putStringArrayList(MyConstants.PASS_DATA, listOFPassword)
            findNavController().navigate(
                R.id.action_passwordFragment_to_setPasswordFragment,
                bundle
            )
            checkUserEntery = true
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