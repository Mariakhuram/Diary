package com.mk.diary.presentation.ui.noteview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mk.diary.R
import com.mk.diary.databinding.FragmentImageViewDetailsBinding
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.companion.Static


class ImageViewDetailsFragment : Fragment() {
    lateinit var binding: FragmentImageViewDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentImageViewDetailsBinding.inflate(layoutInflater,container,false)
        var imageUri=arguments?.getInt(MyConstants.PASS_DATA)
        if (imageUri!=null){
            Glide.with(requireContext()).load(imageUri).into(binding.imageView)
        }
        binding.deleteBtn.setOnClickListener {
            Static.deleteBoolean = true
            imageUri=null
            Glide.with(requireContext()).load(imageUri).into(binding.imageView)
            fragmentManager?.popBackStack()
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
}