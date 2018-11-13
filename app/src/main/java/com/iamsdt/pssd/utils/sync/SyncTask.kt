package com.iamsdt.pssd.utils.sync

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.iamsdt.pssd.BuildConfig
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.Constants.REMOTE.DOWNLOAD_TAG
import com.iamsdt.pssd.utils.Constants.REMOTE.FB_REMOTE_CONFIG_STORAGE_KEY
import com.iamsdt.pssd.utils.SettingsUtils
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.getDayInterval
import com.iamsdt.pssd.utils.ioThread
import com.iamsdt.pssd.utils.sync.worker.DownloadWorker
import com.iamsdt.pssd.utils.sync.worker.UploadWorker
import java.util.*


class SyncTask(private val wordTableDao: WordTableDao,
               private val spUtils: SpUtils,
               private val settingsSpUtils: SettingsUtils) {

    fun initialize(context: Activity) {
        if (!isNetworkAvailable(context)) return

        /*
            isNetworkAvailable method call replaced by constraints
            but if network is not available i don't want to fetch remote config
            but still it can handle no internet issue
         */

        //do this task once in a week
        if (isRunUpload()) {
            ioThread {
                val data = wordTableDao.upload()
                if (data.isNotEmpty()) {
                    //start worker
                    // complete: 8/24/18 worker
                    val request = OneTimeWorkRequest
                            .Builder(UploadWorker::class.java).build()

                    WorkManager.getInstance().beginUniqueWork("Upload",
                            ExistingWorkPolicy.KEEP, request).enqueue()
                } else{
                    //user has no data
                    //deon't need to check again
                    spUtils.uploadDate = Date().time
                }
            }
        }

        //if status is available
        //then download the file
        //do this task once in a week
        if (isRunDownload() && getRemoteConfigStatus(context)) {
            val request = OneTimeWorkRequest
                    .Builder(DownloadWorker::class.java)
                    .addTag(DOWNLOAD_TAG)
                    .build()

            WorkManager.getInstance().beginUniqueWork("Download",
                    ExistingWorkPolicy.KEEP, request).enqueue()
        }
    }


    private fun isRunUpload(): Boolean {
        val date = spUtils.downloadDate
        val interval = getDayInterval(date)

        //if greater than 7 days
        // complete: 8/24/18 make a settings
        return interval >= settingsSpUtils.interval
    }


    private fun isRunDownload(): Boolean {
        val date = spUtils.uploadDate
        val interval = getDayInterval(date)

        //if greater than 7 days
        // complete: 8/24/18 make a settings
        return interval >= settingsSpUtils.interval && settingsSpUtils.shareStatus
    }


    //check for network available
    private fun isNetworkAvailable(context: Activity): Boolean {
        val manager = context.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val info: NetworkInfo? = manager.activeNetworkInfo

        return info != null && info.isConnected
    }


    //get remote config is data fetch is enable or not
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

        return remoteConfig.getBoolean(FB_REMOTE_CONFIG_STORAGE_KEY)
    }
}