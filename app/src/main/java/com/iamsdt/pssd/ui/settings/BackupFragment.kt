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
import android.os.Environment
import androidx.preference.PreferenceFragmentCompat
import com.codekidlabs.storagechooser.StorageChooser
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.settings.SettingsFragment.Companion.bindPreferenceSummaryToValue
import com.iamsdt.pssd.utils.Constants.IO.IMPORT_ADD
import com.iamsdt.pssd.utils.Constants.IO.IMPORT_FAV
import com.iamsdt.pssd.utils.FileImportExportUtils
import com.iamsdt.pssd.utils.SettingsUtils
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject

class BackupFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener{

    //permission code
    private val PERMISSIONS_REQUEST_READ_STORAGE_FAVOURITE = 12
    private val PERMISSIONS_REQUEST_READ_STORAGE_ADDED = 14
    private val PERMISSIONS_REQUEST_WRITE_STORAGE_FAVOURITE = 23
    private val PERMISSIONS_REQUEST_WRITE_STORAGE_ADDED = 36

    @Inject
    lateinit var utils: FileImportExportUtils

    @Inject
    lateinit var settingUtils: SettingsUtils

    var path = ""

    override fun onSharedPreferenceChanged(sp: SharedPreferences?, key: String?) {
        findPreference(key)?.let {
            bindPreferenceSummaryToValue(it)
        }
    }

    // complete: 8/17/2018 inject here
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_backup)


        val count = preferenceScreen.preferenceCount

        (0 until count)
                .asSequence()
                .map { preferenceScreen.getPreference(it) }
                .forEach {
                    bindPreferenceSummaryToValue(it)
                }


        //my all preference
        val exportFavourite = findPreference(getString(R.string.bps_ex_fav_key))
        val importFavourite = findPreference(getString(R.string.bps_im_fav_key))
        val exportAddWord = findPreference(getString(R.string.bps_ex_add_key))
        val importAddWord = findPreference(getString(R.string.bps_im_add_key))

        path = settingUtils.getPath

        exportFavourite.summary = "File saved on $path directory"
        exportAddWord.summary = "File saved on $path directory"


        //export favourite
        exportFavourite.setOnPreferenceClickListener {

            if (Build.VERSION.SDK_INT >= 23) {
//                if (PackageManager.PERMISSION_GRANTED !=
////                        context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
////
////                }
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_WRITE_STORAGE_FAVOURITE)

            } else {
                writeFavouriteData()
            }
            false
        }

        //import favourite
        importFavourite.setOnPreferenceClickListener { preference ->
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_READ_STORAGE_FAVOURITE)
            } else {
                readFavouriteData()
            }

            false
        }

        //export added word
        exportAddWord.setOnPreferenceClickListener { preference ->

            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_WRITE_STORAGE_ADDED)
            } else {
                writeAddedWord()
            }

            false
        }

        //import added word
        importAddWord.setOnPreferenceClickListener { preference ->

            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_READ_STORAGE_ADDED)
            } else {
                readAddedWord()
            }

            false
        }

    }

    //read write favourite data
    @Suppress("DEPRECATION")
    private fun readFavouriteData() {
        Timber.i("call")
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {

            val builder = StorageChooser.Builder()
            builder.withActivity(activity)
            //this is deprecated
            builder.withFragmentManager(activity!!.fragmentManager)
            builder.withMemoryBar(true)
            builder.allowCustomPath(true)
            builder.setType(StorageChooser.FILE_PICKER)
            builder.setDialogTitle("Select file")

            //if path is not empty then save that path as default path
            if (path.isNotEmpty()) {
                builder.withPredefinedPath(path)
            }

            val chooser = builder.build()

            // Show dialog
            chooser.show()

            // get path that the user has chosen
            chooser.setOnSelectListener { path ->
                utils.importFile(path,IMPORT_FAV)
            }


        } else showMessage()
    }

    private fun writeFavouriteData() {
        Timber.i("call")
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            utils.exportFileFavourite()

        } else showMessage()
    }

    //readWrite add word
    private fun writeAddedWord() {
        Timber.i("call")
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            utils.exportFileUser()
        } else showMessage()
    }

    @Suppress("DEPRECATION")
    private fun readAddedWord() {
        Timber.i("call")
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {

            val builder = StorageChooser.Builder()
            builder.withActivity(activity)
            builder.withFragmentManager(activity!!.fragmentManager)
            builder.withMemoryBar(true)
            builder.allowCustomPath(true)
            builder.setType(StorageChooser.FILE_PICKER)
            builder.setDialogTitle("Select file")

            //if path is not null then save that path as default path
            if (path.isNotEmpty()) {
                builder.withPredefinedPath(path)
            }

            val chooser = builder.build()

            // Show dialog whenever you want by
            chooser.show()

            // get path that the user has chosen
            chooser.setOnSelectListener { path ->
                utils.importFile(path, IMPORT_ADD)
            }

        } else showMessage()
    }

    private fun showMessage() {
        val txt = "Something went wrong. " +
                "Your storage is not readable and writable. " +
                "Your storage option is different from others device." +
                "Make sure you grant permission"
        showToast(ToastType.WARNING, txt)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {

        val perTxt = "Oh! you did not give the permission to access storage."

        when (requestCode) {
            PERMISSIONS_REQUEST_READ_STORAGE_FAVOURITE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission added
                    readFavouriteData()
                } else {
                    val txt = "$perTxt To import your favourite word, you must grant permission"
                    showToast(ToastType.WARNING, txt)
                }

            PERMISSIONS_REQUEST_READ_STORAGE_ADDED ->

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readAddedWord()

                } else {
                    val txt = "$perTxt To import your added word, you must grant permission"
                    showToast(ToastType.WARNING, txt)
                }

            PERMISSIONS_REQUEST_WRITE_STORAGE_FAVOURITE ->

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeFavouriteData()

                } else {
                    val txt = "$perTxt To backup your favourite word, you must grant permission"
                    showToast(ToastType.WARNING, txt)
                }

            PERMISSIONS_REQUEST_WRITE_STORAGE_ADDED ->

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeAddedWord()

                } else {
                    val txt = "$perTxt To backup your added word, you must grant permission"
                    showToast(ToastType.WARNING, txt)
                }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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