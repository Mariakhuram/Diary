package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mk.diary.R
import com.mk.diary.databinding.FragmentSetPasswordDisplayBinding


class SetPasswordDisplayFragment : Fragment() {
    lateinit var binding: FragmentSetPasswordDisplayBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSetPasswordDisplayBinding.inflate(layoutInflater)
        binding.backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        binding.setpaswordBtn.setOnClickListener {
            findNavController()
                .navigate(R.id.action_setPasswordDisplayFragment2_to_getReadyFragment2)
        }
        return binding.root
    }

}