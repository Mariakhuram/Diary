package com.mk.diary.adapters.tabslayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mk.diary.presentation.ui.calendertabs.AprFragment
import com.mk.diary.presentation.ui.calendertabs.AugFragment
import com.mk.diary.presentation.ui.calendertabs.DecFragment
import com.mk.diary.presentation.ui.calendertabs.FebFragment
import com.mk.diary.presentation.ui.calendertabs.JanFragment
import com.mk.diary.presentation.ui.calendertabs.JulFragment
import com.mk.diary.presentation.ui.calendertabs.JunFragment
import com.mk.diary.presentation.ui.calendertabs.MarFragment
import com.mk.diary.presentation.ui.calendertabs.MayFragment
import com.mk.diary.presentation.ui.calendertabs.NovFragment
import com.mk.diary.presentation.ui.calendertabs.OctFragment
import com.mk.diary.presentation.ui.calendertabs.SepFragment

class CalenderTabLayoutAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // Return the total number of fragments
        return 12 // Change this based on the number of tabs
    }
    override fun createFragment(position: Int): Fragment {
        // Return the fragment instance for each position
        return when (position) {
            0 -> DecFragment()

            1 -> JanFragment()
            //feb 28
            2 -> FebFragment()
            // mar 31
            3 -> MarFragment()
            //apr 30
            4 -> AprFragment()
            //may 31
            5 -> MayFragment()
            //jun 30
            6 -> JunFragment()
            //jul
            7 -> JulFragment()
            //aug 31
            8 -> AugFragment()
            //sep
            9 -> SepFragment()
            //oct 31
            10 -> OctFragment()
            //nov 30
            11 -> NovFragment()
            //dec 31
            // Add more cases for additional tabs
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}