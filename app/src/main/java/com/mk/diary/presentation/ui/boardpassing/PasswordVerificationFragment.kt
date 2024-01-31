package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.appext.shortToast
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentPasswordVerificationBinding


@AndroidEntryPoint
class PasswordVerificationFragment : Fragment() {
    lateinit var binding: FragmentPasswordVerificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPasswordVerificationBinding.inflate(inflater,container,false)
        val savedTheme = SharedPrefObj.getAppTheme(requireContext())
        binding.confirmBtn.setCardBackgroundColor(savedTheme.color)
        if (savedTheme.theme?.isNotEmpty()==true) {
            Glide.with(requireContext()).load(savedTheme.theme)
                .into(binding.backGroundTheme)
        }
        val receivedPassword=arguments?.getStringArrayList(MyConstants.PASS_DATA)
            saveEmail(receivedPassword)
        setQuestion()
        return binding.root
    }
    private fun setQuestion(){
        binding.spinnerOneBtn.setOnClickListener {
            binding.spinnerOneBtn.visibility=View.INVISIBLE
            binding.spinnerTwoBtn.visibility=View.VISIBLE
            binding.emailEd.visibility=View.INVISIBLE
        }
        binding.qOneBtn.setOnClickListener {

            binding.spinnerOneBtn.setText(resources.getString(R.string.qone))
            binding.spinnerOneBtn.visibility=View.VISIBLE
            binding.spinnerTwoBtn.visibility=View.INVISIBLE
            binding.emailEd.visibility=View.VISIBLE
        }
        binding.qThreeBtn.setOnClickListener {

            binding.spinnerOneBtn.setText(resources.getString(R.string.qthree))
            binding.spinnerOneBtn.visibility=View.VISIBLE
            binding.spinnerTwoBtn.visibility=View.INVISIBLE
            binding.emailEd.visibility=View.VISIBLE
        }
        binding.qTwoBtn.setOnClickListener {
            binding.spinnerOneBtn.setText(resources.getString(R.string.qtwo))
            binding.spinnerOneBtn.visibility=View.VISIBLE
            binding.spinnerTwoBtn.visibility=View.INVISIBLE
            binding.emailEd.visibility=View.VISIBLE
        }
    }

    private fun saveEmail(receivedPassword: ArrayList<String>?) {
        val savedPasswordList = SharedPrefObj.getPasswordList(requireContext())
        val savedEmail = SharedPrefObj.getEmail(requireContext())
        when {
            savedPasswordList?.isNotEmpty() == true && savedEmail != null -> {
                // Case when both savedPasswordList is not empty and savedEmail is not null
                binding.confirmBtn.setOnClickListener {
                    val email = binding.emailEd.text.toString()
                    if (email.isEmpty()) {
                        binding.emailEd.error = resources.getString(R.string.pleaseEnterEmail)
                    } else {
                        if (email == savedEmail) {
                            val bundle = Bundle()
                            bundle.putString(MyConstants.PASS_DATA, "True")
                            // Email matches the saved email
                            SharedPrefObj.removePasswordList(requireContext())
                            findNavController().navigate(R.id.action_passwordVerificationFragment_to_passwordFragment,bundle)
                        } else {
                            showSnackbar(resources.getString(R.string.incorrectEmail))
                        }
                    }
                }
            }

            savedEmail != null && savedPasswordList.isNullOrEmpty() -> {
                // Case when savedEmail is not null and savedPasswordList is null or empty
                binding.confirmBtn.setOnClickListener {
                    val email = binding.emailEd.text.toString()
                    if (email.isEmpty()) {
                        binding.emailEd.error = resources.getString(R.string.pleaseEnterEmail)
                    } else {
                        if (email == savedEmail) {
                            val bundle = Bundle()
                            bundle.putString(MyConstants.PASS_DATA, "True")
                            findNavController().navigate(R.id.action_passwordVerificationFragment_to_passwordFragment, bundle)
                        } else {
                            showSnackbar(resources.getString(R.string.incorrectEmail))
                        }
                    }
                }
            }

            else -> {
                // Default case, when savedPasswordList is null or empty and savedEmail is null
                binding.confirmBtn.setOnClickListener {
                    val email = binding.emailEd.text.toString()
                    if (email.isEmpty()) {
                        binding.emailEd.error = resources.getString(R.string.emailHint)
                    } else {
                        try {
                            SharedPrefObj.savePasswordList(requireContext(), receivedPassword!!)
                            SharedPrefObj.saveEmail(requireContext(), email)
                            Log.d("notifyValue", "ver: "+SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false))
                            if(SharedPrefObj.getBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)){
                                requireContext().newScreen(BottomNavActivity::class.java)
                                Static.passwordReset = "Reset"
                                SharedPrefObj.saveBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)
                            }else{
                                findNavController().navigate(R.id.action_passwordVerificationFragment_to_getReadyFragment3)
                                SharedPrefObj.saveBoolean(requireContext(),MyConstants.SKIP_TOKEN,false)
                            }
                        } catch (e: Exception) {
                            // Handle exception if needed
                        }
                    }
                }
            }
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