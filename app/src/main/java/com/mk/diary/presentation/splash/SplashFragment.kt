package com.mk.diary.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mk.diary.presentation.ui.boardpassing.BoardPassActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.FragmentSplashBinding
@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val splashDuration: Long = 2000 // 2 seconds
    lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSplashBinding.inflate(layoutInflater,container,false)

        if (Static.checkLangBoolean){
            splash()
        }
        return binding.root
    }
    private fun splash() {
        SharedPrefObj.saveBoolean(requireContext(), MyConstants.KEY_HAS_SEEN_BOTTOM_SHEET,false)
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashDuration)
            if (SharedPrefObj.getToken(requireContext())!=null){
                val i= Intent(requireContext(),BoardPassActivity::class.java)
                i.putExtra(MyConstants.PASS_DATA,"P")
                startActivity(i)
            }else{
                findNavController().navigate(R.id.action_splashFragment2_to_selectLanguageFragment2)
            }

        }
    }
}