package com.mk.diary.presentation.ui.calendertabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mk.diary.R
import com.mk.diary.databinding.FragmentJanBinding
import com.mk.mydiary.utils.SharedPrefObj


class JanFragment : Fragment() {

    lateinit var binding: FragmentJanBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentJanBinding.inflate(layoutInflater,container,false)
        binding.day10.setOnClickListener {
            SharedPrefObj.saveString(requireContext(),"k","10")
        }
        binding.day11.setOnClickListener {
            SharedPrefObj.saveString(requireContext(),"k","11")
        }
        binding.day12.setOnClickListener {
            SharedPrefObj.saveString(requireContext(),"k","12")
        }
        return binding.root
    }
}