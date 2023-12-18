package com.mk.diary.presentation.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mk.diary.R
import com.mk.diary.databinding.FragmentSplashBinding
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.SharedPrefObj
import com.mk.mydiary.utils.appext.newScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val splashDuration: Long = 2000 // 2 seconds
    lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSplashBinding.inflate(layoutInflater,container,false)
        splash()
        return binding.root
    }
    private fun splash() {
        SharedPrefObj.saveBoolean(requireContext(),MyConstants.KEY_HAS_SEEN_BOTTOM_SHEET,false)
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashDuration)
            if (SharedPrefObj.getToken(requireContext())!=null){
                requireContext().newScreen(BottomNavActivity::class.java)
                requireActivity().finish()
            }else{
                findNavController().navigate(R.id.action_splashFragment2_to_selectLanguageFragment2)
            }

        }
    }
}