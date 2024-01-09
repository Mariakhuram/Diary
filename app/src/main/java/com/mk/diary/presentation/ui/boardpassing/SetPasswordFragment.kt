package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentSetPasswordBinding

@AndroidEntryPoint
class SetPasswordFragment : Fragment() {
    private val maxImages =4
    private val maxDigits =10
    private val imageViewList = ArrayList<ImageView>()
    private val listOFPassword:ArrayList<String> by lazy { ArrayList() }
    lateinit var binding: FragmentSetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSetPasswordBinding.inflate(inflater,container,false)
        if (SharedPrefObj.getAppTheme(requireContext())!=null){
            val model= SharedPrefObj.getAppTheme(requireContext())
            Glide.with(requireContext()).load(model.theme)
                .into(binding.backGroundTheme)
        }
        val receivedPassword=arguments?.getStringArrayList(MyConstants.PASS_DATA)
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
        setButtonClickListeners(receivedPassword)
        // Set onClickListener for back button
        binding.backMinosBtn.setOnClickListener {
            handleBackButtonClick()
        }
        return binding.root
    }
    private fun setButtonClickListeners(receivedPassword: java.util.ArrayList<String>?) {
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
                handleButtonClick(i + 1,receivedPassword)
            }
        }
    }

    private fun handleButtonClick(value: Int, receivedPassword: java.util.ArrayList<String>?) {
        if (listOFPassword.size < maxImages) {
            // Adjust value to match the button text (1 to 9, 0)
            val adjustedValue = if (value == 10) "0" else value.toString()

            listOFPassword.add(adjustedValue)

            val resId = R.drawable.black_cir_sh
            imageViewList[listOFPassword.size - 1].setImageResource(resId)

            if (listOFPassword.size == maxImages) {
                // Password is complete, navigate to the new screen
                navigateToNewScreen(listOFPassword,receivedPassword)
            }
        } else {

        }
    }    private fun handleBackButtonClick() {
        if (listOFPassword.isNotEmpty()) {
            listOFPassword.removeAt(listOFPassword.size - 1)

            val lastEnteredIndex = listOFPassword.size
            imageViewList[lastEnteredIndex].setImageResource(R.drawable.black_circle_sh)
        } else {
        }
    }

    private fun navigateToNewScreen(
        listOFPassword: ArrayList<String>,
        receivedPassword: java.util.ArrayList<String>?
    ) {
        val enteredPassword = listOFPassword.joinToString(separator = "")
        if (SharedPrefObj.getEmail(requireContext())!=null){
            if (Static.passwordReset=="Reset"){
                if (enteredPassword == receivedPassword?.joinToString(separator = "")) {
                    SharedPrefObj.savePasswordList(requireContext(), receivedPassword!!)
                    requireContext().newScreen(BottomNavActivity::class.java)
                } else {
                    // Passwords don't match, show a toast
                    showSnackbar(resources.getString(R.string.incorrectPass))
                }
            }else{
                if (enteredPassword == receivedPassword?.joinToString(separator = "")) {
                    SharedPrefObj.savePasswordList(requireContext(), receivedPassword!!)
                    requireContext().newScreen(BottomNavActivity::class.java)
                } else {
                    // Passwords don't match, show a toast
                    showSnackbar(resources.getString(R.string.incorrectPass))
                }

            }
        }else{
            if (enteredPassword == receivedPassword?.joinToString(separator = "")) {
                // Passwords match, do something (e.g., navigate to a success screen)
                val bundle=Bundle()
                bundle.putStringArrayList(MyConstants.PASS_DATA,listOFPassword)
                findNavController().navigate(R.id.action_setPasswordFragment_to_passwordVerificationFragment,bundle)
            } else {
                // Passwords don't match, show a toast
                showSnackbar(resources.getString(R.string.incorrectPass))
            }
        }
    }
    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

}