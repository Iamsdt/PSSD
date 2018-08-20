/*
 * Developed By Shudipto Trafder
 * on 8/17/18 11:09 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.settings

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.codekidlabs.storagechooser.StorageChooser
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.utils.Constants.Settings.DEFAULT_PATH_STORAGE
import com.iamsdt.pssd.utils.Constants.Settings.STORAGE_PATH_KEY
import com.iamsdt.pssd.utils.SettingsUtils
import dagger.android.support.AndroidSupportInjection
import java.io.File
import javax.inject.Inject

class AdvanceFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    private val PERMISSIONS_REQUEST_READ_STORAGE = 0
    private var changeDirPref: Preference? = null

    @Inject
    lateinit var settingUtils: SettingsUtils

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        findPreference(key)?.let {
            if (key == getString(R.string.switchShare)){
                val value = sp?.getBoolean(
                        getString(R.string.switchShare),true) ?: true
                val pre = findPreference(getString(R.string.NameShare))
                if (!value){
                    pre?.isEnabled = false
                    pre.shouldDisableView = true
                } else{
                    pre.shouldDisableView = false
                    pre?.isEnabled = true
                }
            }
            bindPreferenceSummaryToValue(it)
        }
    }

    private val sBindPreferenceSummaryToValueListener
            = Preference.OnPreferenceChangeListener { preference, value ->
        val stringValue = value.toString()
        if (preference is CheckBoxPreference){
            preference.summary = stringValue
        } else{
            preference.summary = "path: stringValue"
        }
        true
    }


    private fun bindPreferenceSummaryToValue(preference: Preference) {
        // Set the listener to watch for value changes.
        preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

        // Trigger the listener immediately with the preference's
        // current value.
        //if preference cheekbook
        if (preference is CheckBoxPreference){
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getBoolean(preference.key, false))
        } else{
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_advance)

        changeDirPref = findPreference(getString(R.string.advance_dir_add_key))

        //load from sp
        changeDirPref?.summary = "path: ${settingUtils.getPath}"

        changeDirPref?.setOnPreferenceClickListener {

            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_READ_STORAGE)
            } else {
                selectDir()
            }

            true
        }

        val value = settingUtils.shareStatus
        if (!value){
            val pre = findPreference(getString(R.string.NameShare))
            pre?.isEnabled = false
            pre.shouldDisableView = true
        }


    }

    @Suppress("DEPRECATION")
    private fun selectDir() {

        val file = File(DEFAULT_PATH_STORAGE)

        if (!file.exists()) {
            file.mkdirs()
        }

        val sp = activity
                ?.getSharedPreferences(STORAGE_PATH_KEY, Context.MODE_PRIVATE)


        val chooser = StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(activity?.fragmentManager)
                .withMemoryBar(true)
                .allowCustomPath(true)
                .setDialogTitle("Select a Directory")
                .setType(StorageChooser.DIRECTORY_CHOOSER)
                .withPredefinedPath(file.absolutePath)
                .actionSave(true)
                .withPreference(sp)
                .build()

        // Show dialog whenever you want by
        chooser.show()

        // get path that the user has chosen
        chooser.setOnSelectListener { this.refreshSummery(it) }
    }

    private fun refreshSummery(s: String) {
        if (changeDirPref != null) {
            changeDirPref?.summary = "path: $s"
            //save on sp
            settingUtils.savePath(s)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_READ_STORAGE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission added
                selectDir()
            } else {
                val txt = "Oh! you did not give the permission to access storage." +
                        " To backup your added word, you must grant permission"
                showToast(ToastType.WARNING, txt)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}