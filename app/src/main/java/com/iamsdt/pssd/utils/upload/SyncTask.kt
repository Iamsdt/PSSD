package com.iamsdt.pssd.utils.upload

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.iamsdt.pssd.BuildConfig
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ui.service.UploadService
import com.iamsdt.pssd.utils.Constants.REMOTE.ADMIN
import com.iamsdt.pssd.utils.Constants.REMOTE.FB_REMOTE_CONFIG_STORAGE_KEY
import com.iamsdt.pssd.utils.model.OutputModel
import timber.log.Timber
import java.io.File
import java.util.*


class SyncTask(private val wordTableDao: WordTableDao,
               val gson: Gson) {

    fun initialize(context: Activity) {
        if (!isNetworkAvailable(context)) return

        if (getRemoteConfigStatus(context)) {
            AsyncTask.execute {
                val data = wordTableDao.upload()
                if (data.isNotEmpty()) {
                    //do upload task
                    //first login to the firebase
                    context.startService(Intent(context, UploadService::class.java))
                }
            }
        }
    }

    private fun getFile() {
        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference.child(ADMIN)

        val file = File.createTempFile("download", ".json")

        ref.getFile(file).addOnSuccessListener {
            val data = gson.fromJson(file.bufferedReader(bufferSize = 4096),
                    OutputModel::class.java)

            data?.let {
                AsyncTask.execute {
                    var insert = 0L
                    it.list.map { insert = wordTableDao.add(it) }

                    if (insert > 0) {
                        // TODO: 8/23/18 set to sp
                    }
                }
            }
        }
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