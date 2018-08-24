package com.iamsdt.pssd.utils.upload

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.iamsdt.pssd.BuildConfig
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.Constants.REMOTE.FB_REMOTE_CONFIG_STORAGE_KEY
import com.iamsdt.pssd.utils.DateUtils
import com.iamsdt.pssd.utils.SettingsUtils
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.worker.DownloadWorker
import com.iamsdt.pssd.utils.worker.UploadWorker
import timber.log.Timber
import java.util.*


class SyncTask(private val wordTableDao: WordTableDao,
               private val gson: Gson,
               private val spUtils: SpUtils,
               private val settingsSpUtils:SettingsUtils) {

    fun initialize(context: Activity) {
        if (!isNetworkAvailable(context)) return

        //do this task once in a week
        if (isRunUpload()) {
            AsyncTask.execute {
                val data = wordTableDao.upload()
                if (data.isNotEmpty()) {
                    //start worker
                    // complete: 8/24/18 worker
                    val request = OneTimeWorkRequest
                            .Builder(UploadWorker::class.java).build()
                    WorkManager.getInstance().beginUniqueWork("Upload",
                            ExistingWorkPolicy.REPLACE, request).enqueue()
                }
            }
        }

        //if status is available
        //then download the file
        //do this task once in a week
        if (isRunDownload()) {
            if (getRemoteConfigStatus(context)) {
                val request = OneTimeWorkRequest
                        .Builder(DownloadWorker::class.java).build()
                WorkManager.getInstance().beginUniqueWork("Download",
                        ExistingWorkPolicy.REPLACE, request).enqueue()
            }
        }
    }

    private fun isRunUpload(): Boolean {
        val date = spUtils.dateUpload
        val interval = DateUtils.getDayInterval(date)

        //if greater than 7 days
        // complete: 8/24/18 make a settings
        return interval >= settingsSpUtils.interval()
    }

    private fun isRunDownload(): Boolean {
        val date = spUtils.dateDownload
        val interval = DateUtils.getDayInterval(date)

        //if greater than 7 days
        // complete: 8/24/18 make a settings
        return interval >= settingsSpUtils.interval()
    }

    private fun isNetworkAvailable(context: Activity): Boolean {
        val manager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val info: NetworkInfo? = manager.activeNetworkInfo

        return info != null && info.isConnected
    }

    private fun getRemoteConfigStatus(context: Activity): Boolean {

        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val remoteSetting = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()

        remoteConfig.setConfigSettings(remoteSetting)

        val objectMap = HashMap<String, Any>()
        objectMap[FB_REMOTE_CONFIG_STORAGE_KEY] = false

        remoteConfig.setDefaults(objectMap)

        var cacheExpiration = 3600L // 1 hour in seconds

        if (remoteSetting.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        val fetch = remoteConfig.fetch(cacheExpiration)

        fetch.addOnSuccessListener(context) {
            remoteConfig.activateFetched()
        }

        val state = remoteConfig.getBoolean(FB_REMOTE_CONFIG_STORAGE_KEY)

        //debugOnly:8/23/18 Debug only remove latter
        Timber.i("Remote data: $state")

        return state
    }
}