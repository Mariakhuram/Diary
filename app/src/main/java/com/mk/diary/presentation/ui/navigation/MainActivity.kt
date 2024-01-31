package com.mk.diary.presentation.ui.navigation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.mk.diary.localization.ForLanguageSettingsClass
import com.mk.diary.utils.MyConstants
import dagger.hilt.android.AndroidEntryPoint
import my.dialy.dairy.journal.dairywithlock.R
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val i =intent.getStringExtra(MyConstants.PASS_DATA)
        if (i=="Loc"){
            navigateToFragment(R.id.selectLanguageFragment2)
        }
        if (i=="SetLan"){
            navigateToFragment(R.id.selectLanguageFragment2)
        }
    }
    private fun navigateToFragment(destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(destinationId)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { ForLanguageSettingsClass.onAttach(it) })
    }
}