package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mk.diary.R
import com.mk.diary.databinding.FragmentGetReadyBinding


class GetReadyFragment : Fragment() {
    lateinit var binding: FragmentGetReadyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentGetReadyBinding.inflate(layoutInflater,container,false)
        binding.setpaswordBtn.setOnClickListener {
            findNavController().navigate(R.id.action_getReadyFragment2_to_letsStartFragment2)
        }
        return binding.root
    }

}