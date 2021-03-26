package com.example.tmdb.view

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tmdb.*

/**
 * Handle display of Fragment
 */
class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        Log.d("ViewPagerAdapter", "Create Fragment $position")

        return when (position) {
            MainFragment::class.uiOrder() -> MainFragment()
            SettingsFragment::class.uiOrder() -> SettingsFragment()
            else -> DetailFragment()
        }
    }

    override fun getItemCount(): Int {
        return Common.UI_ORDER.size
    }
}