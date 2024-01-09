package com.mk.diary.presentation.ui.boardpassing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.appext.newScreen
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentLetsStartBinding


class LetsStartFragment : Fragment() {
    lateinit var binding: FragmentLetsStartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLetsStartBinding.inflate(layoutInflater,container,false)
        binding.welcomeBtn.setOnClickListener {
            SharedPrefObj.saveAuthToken(requireContext(), MyConstants.TOKEN)
           requireContext().newScreen(BottomNavActivity::class.java) }
        return binding.root
    }

}