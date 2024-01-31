package com.mk.diary.password

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.mk.diary.di.application.HiltApplication
import com.mk.diary.localization.SharedPref
import com.mk.diary.utils.MyConstants
import com.mk.diary.utils.SharedPrefObj


fun String.printIt() {
    Log.e("-->", this)
}
fun Context.isShowPasswordScreen(show: Boolean){

    HiltApplication.showPasswordScreen = show

}

class ShowPasswordScreen(var application: Application) : Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    init {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                val sharedPref = SharedPref(application)
                if (!SharedPrefObj.getBoolean(application,"isInterAdShow",false)) {
                    // Show password screen directly
                    showPasswordScreen(HiltApplication.showPasswordScreen)
                }
            }
        })
    }
    private fun showPasswordScreen(show: Boolean) {
        // Add code here to navigate or show the password screen
        // If using Navigation component, find the NavController and navigate to the password screen
        if (show && !SharedPrefObj.getBoolean(application,MyConstants.SKIP_TOKEN,false)){
            currentActivity?.let { activity ->
                if (activity is AppCompatActivity) {
                    // Check if the fragment is not already added to avoid duplication

                        // Use the supportFragmentManager to show the PasswordFragment
                        PasswordDialogueFragment().show(activity.supportFragmentManager,"Password")

                }
            }
        }
    }
    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

}
