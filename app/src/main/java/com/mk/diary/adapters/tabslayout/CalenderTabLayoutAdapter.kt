package com.mk.diary.adapters.tabslayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mk.diary.presentation.ui.calendertabs.AprFragment
import com.mk.diary.presentation.ui.calendertabs.FebFragment
import com.mk.diary.presentation.ui.calendertabs.JanFragment

class CalenderTabLayoutAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // Return the total number of fragments
        return 12 // Change this based on the number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        // Return the fragment instance for each position
        return when (position) {
            //31 jan
            0 -> JanFragment() // Replace with your BackgroundFragment class
            //feb 28
            1 -> FebFragment()
            // mar 31
            2 -> JanFragment()
            //apr 30
            3 -> AprFragment()
            //may 31
            4 -> JanFragment()
            //jun 30
            5 -> AprFragment()
            //jul
            6 -> JanFragment()
            //aug 31
            7 -> JanFragment()
            //sep
            8 -> AprFragment()
            //oct 31
            9 -> JanFragment()
            //nov 30
            10 -> AprFragment()
            //dec 31
            11 -> JanFragment()
            // Add more cases for additional tabs
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}