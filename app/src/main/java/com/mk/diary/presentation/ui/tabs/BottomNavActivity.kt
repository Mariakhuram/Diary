package com.mk.diary.presentation.ui.tabs

import android.content.Context
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mk.diary.localization.ForLanguageSettingsClass
import com.mk.diary.presentation.ui.settings.MenuSettingsFragment
import com.mk.diary.utils.SharedPrefObj
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.ActivityBottomNavBinding

@AndroidEntryPoint
class BottomNavActivity : AppCompatActivity() {
    private lateinit var navController:NavController
    private val splashDuration: Long = 3000 // 2 seconds
    lateinit var binding: ActivityBottomNavBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savedTheme = SharedPrefObj.getAppTheme(this)
        if (savedTheme.color != 0 || savedTheme.theme?.isNotEmpty()==true) {
            Glide.with(this).load(savedTheme.theme)
                .into(binding.backGroundTheme)
            val homeDrawable = binding.createNoteFragmentBtn.drawable.mutate()
            homeDrawable.setColorFilter(savedTheme.color, PorterDuff.Mode.SRC_IN)
            binding.createNoteFragmentBtn.setImageDrawable(homeDrawable)
        } else {
            val homeDrawable = binding.createNoteFragmentBtn.drawable.mutate()
            homeDrawable.setColorFilter(resources.getColor(R.color.pinkButtonColor), PorterDuff.Mode.SRC_IN)
            binding.createNoteFragmentBtn.setImageDrawable(homeDrawable)
        }
        Static.checkLangBoolean = true
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerBottomBar) as NavHostFragment
         navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Your code to handle destination changes goes here
            check(destination)
        }
        setDefaultColorForIcon(binding.homeFragmentBtn.id)
        binding.homeFragmentBtn.setOnClickListener {
            if (!isCurrentFragment(HomeFragment::class.java)) {
                // Check if the button is not already selected
                if (!binding.homeFragmentBtn.isSelected) {
                    setDefaultColorForIcon(binding.homeFragmentBtn.id)
                    navigateToFragment(R.id.homeFragment)
                    // Set the button as selected to prevent the "blink" effect
                    binding.menuSettingsFragmentBtn.isSelected = false
                    // Reset the isSelected state for other buttons
                    binding.homeFragmentBtn.isSelected = true
                    binding.galleryFragmentBtn.isSelected = false
                    binding.callenderFragmentBtn.isSelected = false
                }
            }
        }
        binding.galleryFragmentBtn.setOnClickListener {
            if (!isCurrentFragment(GalleryFragment::class.java)) {
                // Check if the button is not already selected
                if (!binding.galleryFragmentBtn.isSelected) {
                    setDefaultColorForIcon(binding.galleryFragmentBtn.id)
                    navigateToFragment(R.id.galleryFragment)
                    // Set the button as selected to prevent the "blink" effect
                    binding.galleryFragmentBtn.isSelected = true
                    // Reset the isSelected state for other buttons
                    binding.homeFragmentBtn.isSelected = false
                    binding.callenderFragmentBtn.isSelected = false
                    binding.menuSettingsFragmentBtn.isSelected = false
                }
            }
        }
        binding.callenderFragmentBtn.setOnClickListener {
            if (!isCurrentFragment(CalenderFragment::class.java)) {
                // Check if the button is not already selected
                if (!binding.callenderFragmentBtn.isSelected) {
                    setDefaultColorForIcon(binding.callenderFragmentBtn.id)
                    navigateToFragment(R.id.calenderFragment2)
                    // Set the button as selected to prevent the "blink" effect
                    binding.callenderFragmentBtn.isSelected = true
                    // Reset the isSelected state for other buttons
                    binding.homeFragmentBtn.isSelected = false
                    binding.galleryFragmentBtn.isSelected = false
                    binding.menuSettingsFragmentBtn.isSelected = false
                }
            }
        }
        binding.menuSettingsFragmentBtn.setOnClickListener {
            if (!isCurrentFragment(MenuSettingsFragment::class.java)) {
                // Check if the button is not already selected
                if (!binding.menuSettingsFragmentBtn.isSelected) {
                    setDefaultColorForIcon(binding.menuSettingsFragmentBtn.id)
                    navigateToFragment(R.id.menuSettingsFragment)
                    // Set the button as selected to prevent the "blink" effect
                    binding.menuSettingsFragmentBtn.isSelected = true
                    // Reset the isSelected state for other buttons
                    binding.homeFragmentBtn.isSelected = false
                    binding.galleryFragmentBtn.isSelected = false
                    binding.callenderFragmentBtn.isSelected = false
                }
            }
        }
        // Set up onClickListener for the "Create Note" button
        binding.createNoteFragmentBtn.setOnClickListener {
            binding.bottomBar.visibility= View.GONE
            binding.createNoteFragmentBtn.visibility= View.GONE
            binding.createSecondBtn.visibility= View.GONE
            findNavController(R.id.fragmentContainerBottomBar).navigate(R.id.createNoteFragment)
        }
        if (Static.settLang=="SetLan"){
            setDefaultColorForIcon(binding.menuSettingsFragmentBtn.id)
            binding.menuSettingsFragmentBtn.isSelected = true
            // Reset the isSelected state for other buttons
            binding.homeFragmentBtn.isSelected = false
            binding.galleryFragmentBtn.isSelected = false
            binding.callenderFragmentBtn.isSelected = false
            isCurrentFragment(MenuSettingsFragment::class.java)
            navigate(R.id.menuSettingsFragment)
        }
        if (Static.passwordReset=="Reset"){
            binding.passwordUpdatedDialogue.visibility=View.VISIBLE
            setDefaultColorForIcon(binding.menuSettingsFragmentBtn.id)
            binding.menuSettingsFragmentBtn.isSelected = true
            // Reset the isSelected state for other buttons
            binding.homeFragmentBtn.isSelected = false
            binding.galleryFragmentBtn.isSelected = false
            binding.callenderFragmentBtn.isSelected = false
            isCurrentFragment(MenuSettingsFragment::class.java)
            navigate(R.id.menuSettingsFragment)
            CoroutineScope(Dispatchers.Main).launch {
                delay(splashDuration)
                Static.passwordReset=null
                binding.passwordUpdatedDialogue.visibility=View.GONE
            }
        }
    }
    private fun navigate(destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerBottomBar) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(destinationId)
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { ForLanguageSettingsClass.onAttach(it) })
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
                binding.createSecondBtn.visibility=View.GONE
                setDefaultColorForIcon(R.id.homeFragment)
                navigate(R.id.homeFragment)
                isCurrentFragment(HomeFragment::class.java)
            }
            R.id.calenderFragment2, R.id.menuSettingsFragment, R.id.galleryFragment -> {
                setDefaultColorForIcon(binding.homeFragmentBtn.id)
                navigateToFragment(R.id.homeFragment)
                binding.bottomBar.visibility = View.VISIBLE
                binding.createNoteFragmentBtn.visibility = View.VISIBLE
                binding.createSecondBtn.visibility=View.VISIBLE
            }
            R.id.homeFragment -> {
                exitBottomSheet()
            }
            R.id.preViewNoteFragment-> {
                binding.bottomBar.visibility = View.GONE
                binding.createNoteFragmentBtn.visibility = View.GONE
                binding.createSecondBtn.visibility=View.GONE
                navController.popBackStack()
            }
            R.id.deleteDatabaseFragment-> {
                binding.bottomBar.visibility = View.GONE
                binding.createNoteFragmentBtn.visibility = View.GONE
                binding.createSecondBtn.visibility=View.GONE
                navController.popBackStack()
                isCurrentFragment(MenuSettingsFragment::class.java)
                setDefaultColorForIcon(R.id.menuSettingsFragment)
            }
            R.id.passwordResetFragment->{
                    binding.bottomBar.visibility = View.GONE
                    binding.createNoteFragmentBtn.visibility = View.GONE
                    binding.createSecondBtn.visibility=View.GONE
                navController.popBackStack()
            }
            R.id.passwordSettingFragment->{
                binding.bottomBar.visibility = View.GONE
                binding.createNoteFragmentBtn.visibility = View.GONE
                binding.createSecondBtn.visibility=View.GONE
                navController.popBackStack()
                isCurrentFragment(MenuSettingsFragment::class.java)
                setDefaultColorForIcon(R.id.menuSettingsFragment)
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
            binding.createSecondBtn.visibility=View.VISIBLE
        }else if (destination.id == R.id.calenderFragment2){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
            binding.createSecondBtn.visibility=View.VISIBLE
        }else if (destination.id == R.id.menuSettingsFragment){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
            binding.createSecondBtn.visibility=View.VISIBLE
        }else if (destination.id == R.id.galleryFragment){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
            binding.createSecondBtn.visibility=View.VISIBLE
        }else if (destination.id==R.id.homeFragment){
            binding.bottomBar.visibility=View.VISIBLE
            binding.createNoteFragmentBtn.visibility=View.VISIBLE
            binding.createSecondBtn.visibility=View.VISIBLE
            //finish
        }else if (destination.id==R.id.preViewNoteFragment){
            binding.bottomBar.visibility=View.GONE
            binding.createNoteFragmentBtn.visibility=View.GONE
            binding.createSecondBtn.visibility=View.GONE
        }else if (destination.id==R.id.noteViewDetailsFragment2) {
            binding.bottomBar.visibility = View.GONE
            binding.createNoteFragmentBtn.visibility = View.GONE
            binding.createSecondBtn.visibility=View.GONE
        }else if (destination.id==R.id.noteViewSearchFragment) {
            binding.bottomBar.visibility = View.GONE
            binding.createNoteFragmentBtn.visibility = View.GONE
            binding.createSecondBtn.visibility=View.GONE
        }else if (destination.id==R.id.deleteDatabaseFragment) {
            binding.bottomBar.visibility = View.GONE
            binding.createNoteFragmentBtn.visibility = View.GONE
            binding.createSecondBtn.visibility=View.GONE

        }else if (destination.id==R.id.appThemeFragment) {
            binding.bottomBar.visibility = View.GONE
            binding.createNoteFragmentBtn.visibility = View.GONE
            binding.createSecondBtn.visibility=View.GONE
        }else if (destination.id==R.id.passwordResetFragment) {
            binding.bottomBar.visibility = View.GONE
            binding.createNoteFragmentBtn.visibility = View.GONE
            binding.createSecondBtn.visibility=View.GONE
        }else if (destination.id==R.id.passwordSettingFragment) {
            binding.bottomBar.visibility = View.GONE
            binding.createNoteFragmentBtn.visibility = View.GONE
            binding.createSecondBtn.visibility=View.GONE
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
    }
    private fun exitBottomSheet() {
        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.delete_bottom_sheet, null)
        val cancelBtn = view.findViewById<TextView>(R.id.cancelBtn)
        val deleteBtn = view.findViewById<TextView>(R.id.deleteBtn)
        val imageRec = view.findViewById<ImageView>(R.id.mainImage)
        val mainDesc = view.findViewById<TextView>(R.id.mainDesc)
        val mainTitle = view.findViewById<TextView>(R.id.mainTitle)
        cancelBtn.setText(resources.getString(R.string.discard))
        mainTitle.setText(resources.getString(R.string.exit))
        mainDesc.setText(resources.getString(R.string.exitDsc))
        imageRec.setImageResource(R.drawable.errorwarningicon)
        cancelBtn.setText(resources.getString(R.string.cancel))
        deleteBtn.setText(resources.getString(R.string.yes))
        deleteBtn.setOnClickListener {
            finishAffinity()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.setOnCancelListener {
            // Handle the cancel listener if needed
        }
        dialog.show()
    }




}