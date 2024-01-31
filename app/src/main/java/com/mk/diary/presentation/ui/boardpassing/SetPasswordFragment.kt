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
    private val listPassword:ArrayList<String> by lazy { ArrayList() }
    lateinit var binding: FragmentSetPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSetPasswordBinding.inflate(inflater,container,false)
        buttonClicks()
        if (SharedPrefObj.getAppTheme(requireContext())!=null){
            val model= SharedPrefObj.getAppTheme(requireContext())
            Glide.with(requireContext()).load(model.theme)
                .into(binding.backGroundTheme)
        }
        val receivedPassword=arguments?.getStringArrayList(MyConstants.PASS_DATA)
        // Set onClickListener for each button
        // Set onClickListener for back button
        binding.backMinosBtn.setOnClickListener {
            handleBackButtonClick()
            updateUi()
        }
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
        val receivedPassword=arguments?.getStringArrayList(MyConstants.PASS_DATA)
        passwordCheck(receivedPassword)
    }
    private fun passwordCheck(receivedPassword: java.util.ArrayList<String>?) {
        if (listPassword.isNotEmpty()){
            if (listPassword.size== 4) {

                navigateToNewScreen(listPassword,receivedPassword)
            } else if (listPassword.size>4){
                listPassword.clear()
                showSnackbar(resources.getString(R.string.maxsizePassword))
            }
        }
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
    private fun handleBackButtonClick() {
        if (listPassword.isNotEmpty()) {
            listPassword.removeAt(listPassword.size - 1)
            updateUi()
        } else {
            showSnackbar(resources.getString(R.string.nodigitstoremove))
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
                    if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                        val bundle=Bundle()
                        bundle.putStringArrayList(MyConstants.PASS_DATA,listOFPassword)
                        findNavController().navigate(R.id.action_setPasswordFragment_to_passwordVerificationFragment,bundle)
                    }else{
                        SharedPrefObj.savePasswordList(requireContext(), receivedPassword!!)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }
                } else {
                    // Passwords don't match, show a toast
                    showSnackbar(resources.getString(R.string.incorrectPass))
                }
            }else{
                if (enteredPassword == receivedPassword?.joinToString(separator = "")) {
                    if (SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                        val bundle=Bundle()
                        bundle.putStringArrayList(MyConstants.PASS_DATA,listOFPassword)
                        findNavController().navigate(R.id.action_setPasswordFragment_to_passwordVerificationFragment,bundle)
                    }else{
                        SharedPrefObj.savePasswordList(requireContext(), receivedPassword!!)
                        requireContext().newScreen(BottomNavActivity::class.java)
                    }
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
    override fun onResume() {
        super.onResume()
        if (listPassword.isNotEmpty()){
            listPassword.clear()
        }
    }
    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

}