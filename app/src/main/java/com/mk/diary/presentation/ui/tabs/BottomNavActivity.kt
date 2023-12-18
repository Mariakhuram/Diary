package com.mk.diary.presentation.ui.tabs

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.mk.diary.R
import com.mk.diary.databinding.ActivityBottomNavBinding
import com.mk.diary.presentation.ui.settings.MenuSettingsFragment
import com.mk.mydiary.utils.MyConstants
import com.mk.mydiary.utils.appext.shortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class BottomNavActivity : AppCompatActivity() {
     var d:NavDestination?=null
    lateinit var binding: ActivityBottomNavBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerBottomBar) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Your code to handle destination changes goes here

            check(destination)
//            }else if (destination.id==R.id.edit){
//                binding.bottomBar.visibility=View.GONE
//                binding.createNoteFragmentBtn.visibility=View.GONE
//            }else if (destination.id==R.id.homeFragment){
//                binding.bottomBar.visibility=View.GONE
//                binding.createNoteFragmentBtn.visibility=View.GONE
//            }
        }
        setDefaultColorForIcon(binding.homeFragmentBtn.id)
        binding.homeFragmentBtn.setOnClickListener {
            if(!isCurrentFragment(HomeFragment::class.java)){
                setDefaultColorForIcon(binding.homeFragmentBtn.id)
                navigateToFragment(R.id.homeFragment)
            }
        }

        binding.galleryFragmentBtn.setOnClickListener {
            if(!isCurrentFragment(GalleryFragment::class.java)){
                setDefaultColorForIcon(binding.galleryFragmentBtn.id)
                navigateToFragment(R.id.galleryFragment)
            }
        }
        binding.callenderFragmentBtn.setOnClickListener {
            if(!isCurrentFragment(CalenderFragment::class.java)){
                setDefaultColorForIcon(binding.callenderFragmentBtn.id)
                navigateToFragment(R.id.calenderFragment2)
            }
        }

        binding.menuSettingsFragmentBtn.setOnClickListener {
            if(!isCurrentFragment(MenuSettingsFragment::class.java)){
                setDefaultColorForIcon(binding.menuSettingsFragmentBtn.id)
                navigateToFragment(R.id.menuSettingsFragment)
            }
        }
        // Set up onClickListener for the "Create Note" button
        binding.createNoteFragmentBtn.setOnClickListener {
            binding.bottomBar.visibility= View.GONE
            binding.createNoteFragmentBtn.visibility= View.GONE
            findNavController(R.id.fragmentContainerBottomBar).navigate(R.id.createNoteFragment)
        }
    }
    private fun isCurrentFragment(fragmentClass: Class<*>): Boolean {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.fragmentContainerBottomBar)
        return fragment != null && fragment.javaClass == fragmentClass
    }
    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerBottomBar) as NavHostFragment
        val navController = navHostFragment.navController
        val currentDestination = navController.currentDestination

        when (currentDestination?.id) {
            R.id.createNoteFragment -> {
                binding.bottomBar.visibility = View.GONE
                binding.createNoteFragmentBtn.visibility = View.GONE
                fragmentManager.popBackStack()
            }
            R.id.calenderFragment2, R.id.menuSettingsFragment, R.id.galleryFragment -> {
                setDefaultColorForIcon(binding.homeFragmentBtn.id)
                navigateToFragment(R.id.homeFragment)
                binding.bottomBar.visibility = View.VISIBLE
                binding.createNoteFragmentBtn.visibility = View.VISIBLE
            }
            R.id.homeFragment -> {
                finish()
            }
            R.id.preViewNoteFragment-> {
//                navigateToFragment(R.id.createNoteFragment)
                binding.bottomBar.visibility = View.GONE
                binding.createNoteFragmentBtn.visibility = View.GONE
            }
            else -> {

                super.onBackPressed()
            }
        }
    }
    private fun setDefaultColorForIcon(viewId: Int) {
        when (viewId) {
            R.id.homeFragmentBtn ->{
                val homeDrawable = binding.homeFragmentBtn.drawable
                homeDrawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
                binding.homeFragmentBtn.setImageDrawable(homeDrawable)

                // Handle galleryFragmentBtn
                val galleryDrawable = binding.galleryFragmentBtn.drawable
                galleryDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.galleryFragmentBtn.setImageDrawable(galleryDrawable)

                // Handle callenderFragmentBtn
                val calendarDrawable = binding.callenderFragmentBtn.drawable
                calendarDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.callenderFragmentBtn.setImageDrawable(calendarDrawable)

                // Handle menuSettingsFragmentBtn
                val settingsDrawable = binding.menuSettingsFragmentBtn.drawable
                settingsDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.menuSettingsFragmentBtn.setImageDrawable(settingsDrawable)
            }
            R.id.galleryFragmentBtn -> {
                val homeDrawable = binding.homeFragmentBtn.drawable
                homeDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.homeFragmentBtn.setImageDrawable(homeDrawable)

                // Handle galleryFragmentBtn
                val galleryDrawable = binding.galleryFragmentBtn.drawable
                galleryDrawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
                binding.galleryFragmentBtn.setImageDrawable(galleryDrawable)

                // Handle callenderFragmentBtn
                val calendarDrawable = binding.callenderFragmentBtn.drawable
                calendarDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.callenderFragmentBtn.setImageDrawable(calendarDrawable)

                // Handle menuSettingsFragmentBtn
                val settingsDrawable = binding.menuSettingsFragmentBtn.drawable
                settingsDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.menuSettingsFragmentBtn.setImageDrawable(settingsDrawable)
            }
            R.id.callenderFragmentBtn -> {
                val homeDrawable = binding.homeFragmentBtn.drawable
                homeDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.homeFragmentBtn.setImageDrawable(homeDrawable)

                // Handle galleryFragmentBtn
                val galleryDrawable = binding.galleryFragmentBtn.drawable
                galleryDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.galleryFragmentBtn.setImageDrawable(galleryDrawable)

                // Handle callenderFragmentBtn
                val calendarDrawable = binding.callenderFragmentBtn.drawable
                calendarDrawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
                binding.callenderFragmentBtn.setImageDrawable(calendarDrawable)

                // Handle menuSettingsFragmentBtn
                val settingsDrawable = binding.menuSettingsFragmentBtn.drawable
                settingsDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.menuSettingsFragmentBtn.setImageDrawable(settingsDrawable)
            }
            R.id.menuSettingsFragmentBtn ->{
                val homeDrawable = binding.homeFragmentBtn.drawable
                homeDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.homeFragmentBtn.setImageDrawable(homeDrawable)

                // Handle galleryFragmentBtn
                val galleryDrawable = binding.galleryFragmentBtn.drawable
                galleryDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.galleryFragmentBtn.setImageDrawable(galleryDrawable)

                // Handle callenderFragmentBtn
                val calendarDrawable = binding.callenderFragmentBtn.drawable
                calendarDrawable.setColorFilter(ContextCompat.getColor(this, R.color.unselected_State_Color), PorterDuff.Mode.SRC_IN)
                binding.callenderFragmentBtn.setImageDrawable(calendarDrawable)

                // Handle menuSettingsFragmentBtn
                val settingsDrawable = binding.menuSettingsFragmentBtn.drawable
                settingsDrawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN)
                binding.menuSettingsFragmentBtn.setImageDrawable(settingsDrawable)
            }
            // Add more cases for other icons as needed
        }
    }
    private fun navigateToFragment(destinationId: Int) {
        findNavController(R.id.fragmentContainerBottomBar).navigate(destinationId)
    }
    private fun check(destination: NavDestination){
        if (destination.id == R.id.createNoteFragment) {
            binding.bottomBar.visibility=View.GONE
            binding.createNoteFragmentBtn.visibility=View.GONE
        }else if (destination.id == R.id.calenderFragment2){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
        }else if (destination.id == R.id.menuSettingsFragment){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
        }else if (destination.id == R.id.galleryFragment){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
        }else if (destination.id==R.id.homeFragment){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
            //finish
        }else if (destination.id==R.id.preViewNoteFragment){
            binding.bottomBar.visibility=View.GONE
            binding.createNoteFragmentBtn.visibility=View.GONE
        }else if (destination.id==R.id.noteViewDetailsFragment2) {
            binding.bottomBar.visibility = View.GONE
            binding.createNoteFragmentBtn.visibility = View.GONE
        }
    }


}