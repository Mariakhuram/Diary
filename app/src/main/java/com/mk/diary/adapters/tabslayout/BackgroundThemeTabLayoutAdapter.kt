package com.mk.diary.adapters.tabslayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mk.diary.presentation.ui.noteview.tablayout.AllBackroundThemeFragment
import com.mk.diary.presentation.ui.noteview.tablayout.ClassicFragment
import com.mk.diary.presentation.ui.noteview.tablayout.ColorFragment
import com.mk.diary.presentation.ui.noteview.tablayout.CuteThemeFragment
import com.mk.diary.presentation.ui.noteview.tablayout.HolidayThemeFragment
import com.mk.diary.presentation.ui.noteview.tablayout.SimpleThemeFragment

class BackgroundThemeTabLayoutAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // Return the total number of fragments
        return 6 // Change this based on the number of tabs
    }

    override fun createFragment(position: Int): Fragment {
        // Return the fragment instance for each position
        return when (position) {
            0 -> AllBackroundThemeFragment() // Replace with your BackgroundFragment class
            1 -> ClassicFragment()     // Replace with your EmojiFragment class
            2 -> SimpleThemeFragment()     // Replace with your FontsFragment class
            3 -> CuteThemeFragment()
            4 -> HolidayThemeFragment()
            5 -> ColorFragment()
            // Add more cases for additional tabs
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}