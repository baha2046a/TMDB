package com.example.tmdb

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    var sharedPreferences: SharedPreferences? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        Log.d("Preferences", "onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Preferences", "onResume")
        context?.also {
            sharedPreferences = it.getSharedPreferences(it.packageName + "_preferences", Context.MODE_PRIVATE).apply {
                registerOnSharedPreferenceChangeListener(Common::changeListener)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("Preferences", "onPause")
        context?.also {
            sharedPreferences?.unregisterOnSharedPreferenceChangeListener(Common::changeListener)
        }
    }
}