package com.mk.diary.presentation.ui.boardpassing

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.mk.diary.localization.ForLanguageSettingsClass
import com.mk.diary.presentation.ui.tabs.BottomNavActivity
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.appext.newScreen
import com.mk.diary.utils.companion.Static
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
import my.dialy.dairy.journal.dairywithlock.databinding.ActivityBoardPassBinding

@AndroidEntryPoint
class BoardPassActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBoardPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val i =intent.getStringExtra(MyConstants.PASS_DATA)
        if (i=="P"){
            navigateToFragment(R.id.passwordFragment)
        }
        if (!Static.checkLangBoolean){
            navigateToFragment(R.id.passwordFragment)
        }
    }
    private fun navigateToFragment(destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(destinationId)
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { ForLanguageSettingsClass.onAttach(it) })
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val currentDestination = navController.currentDestination
        when (currentDestination?.id) {
            R.id.passwordFragment -> {
                if (Static.passwordReset=="Reset"){
                   newScreen(BottomNavActivity::class.java)
                }else{
                    finishAffinity()
                }
            }
            R.id.setPasswordFragment, R.id.passwordVerificationFragment -> {
              navController.popBackStack()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

}