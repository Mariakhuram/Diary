package com.mk.diary.presentation.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mk.diary.presentation.ui.boardpassing.BoardPassActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.companion.Static

import com.mk.diary.utils.companion.Static.PASSWORD_CHANGE
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentPasswordResetBinding


@AndroidEntryPoint
class PasswordResetFragment : Fragment() {
    private val listOFPassword:ArrayList<String> by lazy { ArrayList() }
    lateinit var binding: FragmentPasswordResetBinding
    private val maxImages =4
    private val maxDigits =10
    private val imageViewList = ArrayList<ImageView>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPasswordResetBinding.inflate(inflater,container,false)
        if (SharedPrefObj.getAppTheme(requireContext())!=null){
            val model= SharedPrefObj.getAppTheme(requireContext())
            Glide.with(requireContext()).load(model.theme)
                .into(binding.backGroundTheme)
        }
        imageViewList.addAll(listOf(
            binding.passCodeOne,
            binding.passCodeTwo,
            binding.passCodeThree,
            binding.passCodeFour
        ))

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
        if (SharedPrefObj.getPasswordList(requireContext())==null
            && SharedPrefObj.getEmail(requireContext())!=null){
            forgotPasswordAlert()
        }

        return binding.root
    }
    private fun forgotPasswordAlert(){
        binding.customAlert.visibility=View.VISIBLE
        binding.forgotPasswordLayout.setpaswordBtn.setOnClickListener {
            binding.customAlert.visibility=View.GONE
            val i = Intent(requireContext(), BoardPassActivity::class.java)
            i.putExtra(MyConstants.PASS_DATA, "P")
            Static.passwordReset = "Reset"
            startActivity(i)
        }
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
    }    private fun handleBackButtonClick() {
        if (listOFPassword.isNotEmpty()) {
            listOFPassword.removeAt(listOFPassword.size - 1)

            val lastEnteredIndex = listOFPassword.size
            imageViewList[lastEnteredIndex].setImageResource(R.drawable.black_circle_sh)
        } else {
            showSnackbar(resources.getString(R.string.nodigitstoremove))
        }
    }
    private fun navigateToNewScreen(listOFPassword: ArrayList<String>) {
        if (SharedPrefObj.getEmail(requireContext()) != null
            && SharedPrefObj.getPasswordList(requireContext()) != null
        ) {
            val enteredPassword = listOFPassword.joinToString(separator = "")
            if (enteredPassword == SharedPrefObj.getPasswordList(requireContext())
                    ?.joinToString(separator = "")
            ) {
                // Passwords match, do something (e.g., navigate to a success screen)
               // SharedPrefObj.removePasswordList(requireContext())
                val i = Intent(requireContext(), BoardPassActivity::class.java)
                i.putExtra(MyConstants.PASS_DATA, "P")
                Static.passwordReset = "Reset"
                PASSWORD_CHANGE = true
                startActivity(i)
            } else {
                showSnackbar("Incorrect password. Please try again.")
            }
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}