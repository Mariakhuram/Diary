package com.mk.diary.presentation.ui.noteview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mk.diary.utils.MyConstants
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentNoteViewDetailsImageViewBinding


class NoteViewDetailsImageViewFragment : Fragment() {
    lateinit var binding: FragmentNoteViewDetailsImageViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNoteViewDetailsImageViewBinding.inflate(layoutInflater,container,false)
        var imageUri=arguments?.getString(MyConstants.PASS_DATA)
        if (imageUri!=null){
            Glide.with(requireContext()).load(imageUri).into(binding.imageView)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}