package com.mk.diary.presentation.ui.tabs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentBottomNavigationBinding


class BottomNavigationFragment : Fragment() {
    lateinit var binding: FragmentBottomNavigationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        binding = FragmentBottomNavigationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}

