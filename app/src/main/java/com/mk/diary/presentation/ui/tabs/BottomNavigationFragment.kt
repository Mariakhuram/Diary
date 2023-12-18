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
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.mk.diary.R
import com.mk.diary.databinding.FragmentBottomNavigationBinding
import com.mk.diary.presentation.ui.noteview.CreateNoteFragment
import com.mk.diary.presentation.ui.settings.MenuSettingsFragment
import com.mk.mydiary.utils.appext.shortToast
import com.mk.mydiary.utils.companion.Static


class BottomNavigationFragment : Fragment() {
//    private var selectedIconId: Int? = null
//    private var backPressedHandler: OnBackPressedDispatcher? = null
    lateinit var binding: FragmentBottomNavigationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        binding = FragmentBottomNavigationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        toggleIconColor(binding.homeFragmentBtn,R.drawable.homefragmenticone)
//        replaceFragment(HomeFragment())
//        if (!Static.checkBolean) {
//            changeVectorColor(
//                binding.homeFragmentBtn,
//                resources.getColor(R.color.selected_State_Color),
//                R.drawable.homefragmenticone
//            )
//            Static.checkBolean = true
//            selectedIconId = binding.homeFragmentBtn.id
//        } else {
//            selectedIconId?.let {
//                toggleIconColor(binding.homeFragmentBtn, R.drawable.homefragmenticone)
//            }
//        }
//        bottomNavigation()
//        backPressedHandler = requireActivity().onBackPressedDispatcher
//        setupOnBackPressedCallback()
//    }
//
//    private fun setupOnBackPressedCallback() {
//        backPressedHandler?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                handleBackPressed()
//            }
//        })
//    }
//
//    private fun handleBackPressed() {
//        if (isCurrentFragment(HomeFragment::class.java)) {
//            activity?.finish()
//        } else {
//            replaceFragment(HomeFragment())
//            toggleIconColor(binding.homeFragmentBtn, R.drawable.homefragmenticone)
//        }
//    }
//
//    private fun bottomNavigation() {
//        binding.homeFragmentBtn.setOnClickListener {
//            if (!isCurrentFragment(HomeFragment::class.java)){
//                toggleIconColor(binding.homeFragmentBtn, R.drawable.homefragmenticone)
//                replaceFragment(HomeFragment())
//            }
//        }
//        binding.galleryFragmentBtn.setOnClickListener {
//            if (!isCurrentFragment(GalleryFragment::class.java)){
//                toggleIconColor(binding.galleryFragmentBtn, R.drawable.galleryicone)
//                replaceFragment(GalleryFragment())
//            }
//        }
//
//        binding.createNoteFragmentBtn.setOnClickListener {
//
//        }
//
//        binding.callenderFragmentBtn.setOnClickListener {
//            if (!isCurrentFragment(CalenderFragment::class.java)){
//                toggleIconColor(binding.callenderFragmentBtn, R.drawable.calenderviewicnoe)
//                replaceFragment(CalenderFragment())
//            }
//        }
//
//        binding.menuSettingsFragmentBtn.setOnClickListener {
//            if (!isCurrentFragment(MenuSettingsFragment::class.java)){
//                toggleIconColor(binding.menuSettingsFragmentBtn, R.drawable.menusettingsicnoe)
//                replaceFragment(MenuSettingsFragment())
//            }
//        }
//    }
//
//    private fun isCurrentFragment(fragmentClass: Class<*>): Boolean {
//        val fragmentManager = requireActivity().supportFragmentManager
//        val fragment = fragmentManager.findFragmentById(R.id.fragmentContainer)
//        return fragment != null && fragment.javaClass == fragmentClass
//    }
//
//    private fun toggleIconColor(imageView: ImageView, vector: Int) {
//        selectedIconId?.let { setDefaultColorForIcon(it) }
//        if (selectedIconId == imageView.id) {
//            selectedIconId = null
//            setDefaultColorForIcon(imageView.id)
//        } else {
//            selectedIconId = imageView.id
//            changeVectorColor(imageView, resources.getColor(R.color.selected_State_Color), vector)
//        }
//    }
//    private fun setDefaultColorForIcon(viewId: Int) {
//        when (viewId) {
//            R.id.homeFragmentBtn ->
//
//                changeVectorColor(
//                binding.homeFragmentBtn,
//                resources.getColor(R.color.unselected_State_Color),
//                R.drawable.homefragmenticone
//            )
//            R.id.galleryFragmentBtn ->
//            {
//
//                changeVectorColor(
//                    binding.galleryFragmentBtn,
//                    resources.getColor(R.color.unselected_State_Color),
//                    R.drawable.galleryicone
//                )
//            }
//
//            R.id.callenderFragmentBtn -> changeVectorColor(
//                binding.callenderFragmentBtn,
//                resources.getColor(R.color.unselected_State_Color),
//                R.drawable.calenderviewicnoe
//            )
//            R.id.menuSettingsFragmentBtn -> changeVectorColor(binding.menuSettingsFragmentBtn, resources.getColor(R.color.unselected_State_Color),
//                R.drawable.menusettingsicnoe
//            )
//            // Add more cases for other icons as needed
//        }
//    }
//    private fun changeVectorColor(imageView: ImageView, colorRes: Int, vector: Int) {
//        val vd = VectorDrawableCompat.create(resources, vector, context?.theme)
//        vd?.setTint(colorRes)
//        imageView.setImageDrawable(vd)
//    }
//    private fun replaceFragment(fragment: Fragment) {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragmentContainer, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }
}

