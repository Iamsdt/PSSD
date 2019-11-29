/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:11 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.settings

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.*
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ui.color.ThemeUtils

class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    var state = false

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {

        if (key == getString(R.string.switchNight)) {
            state = sp?.getBoolean(key, false) ?: false
            ThemeUtils.turnOnOFNightMode(context!!, state)
            restartActivity()
        }

        findPreference<Preference>(key as CharSequence)?.let {
            bindPreferenceSummaryToValue(it)
        }
    }

    private fun restartActivity() {
        val restartIntent = Intent(context,
                SettingsActivity::class.java)
        activity?.setResult(Activity.RESULT_OK)

        activity?.finish()

        startActivity(restartIntent)
        activity?.overridePendingTransition(0, 0)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general)

        val count = preferenceScreen.preferenceCount

        (0 until count)
                .asSequence()
                .map { preferenceScreen.getPreference(it) }
                .forEach {
                    bindPreferenceSummaryToValue(it)
                }

        // complete: 8/17/2018 check this
        val backup = findPreference<Preference>(getString(R.string.bps_key))
        val advance = findPreference<Preference>(getString(R.string.advance_key))

        backup?.setSummary(R.string.bps_summery)
        advance?.setSummary(R.string.advance_summery)

        //get night
        state = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(getString(R.string.switchNight), false)

        val pre = findPreference<SwitchPreference>(getString(R.string.switchNight))

        pre?.icon = if (state) context?.getDrawable(R.drawable.ic_wb_sunny_black_24dp)
        else context?.getDrawable(R.drawable.ic_half_moon)

    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {

        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val index = preference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                        if (index >= 0)
                            preference.entries[index]
                        else
                            null)

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
            }
            true
        }


        fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.

            //checkbox and switch pref
            if (preference is SwitchPreference) {
                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                        PreferenceManager
                                .getDefaultSharedPreferences(preference.context)
                                .getBoolean(preference.key, false))
            } else {
                sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                        PreferenceManager
                                .getDefaultSharedPreferences(preference.context)
                                .getString(preference.key, ""))
            }
        }
    }

}