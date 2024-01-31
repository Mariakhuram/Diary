package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentSetPasswordDisplayBinding


class SetPasswordDisplayFragment : Fragment() {
    lateinit var binding: FragmentSetPasswordDisplayBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSetPasswordDisplayBinding.inflate(layoutInflater)
//        binding.backBtn.setOnClickListener {
//            fragmentManager?.popBackStack()
//        }
        binding.skipBtn.setOnClickListener {
            requireContext().newScreen(BottomNavActivity::class.java)
            SharedPrefObj.saveBoolean(requireContext(),MyConstants.SKIP_TOKEN,true)
        }
        binding.setpaswordBtn.setOnClickListener {
            findNavController().navigate(R.id.action_setPasswordDisplayFragment_to_passwordFragment)
        }
        return binding.root
    }


}