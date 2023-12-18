package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mk.diary.R
import com.mk.diary.databinding.FragmentSelectLanguageBinding


class SelectLanguageFragment : Fragment() {
    lateinit var binding: FragmentSelectLanguageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSelectLanguageBinding.inflate(layoutInflater,container,false)
        binding.nextBtn.setOnClickListener {
            findNavController().navigate(R.id.action_selectLanguageFragment2_to_setPasswordDisplayFragment2)
        }
        return binding.root
    }
}