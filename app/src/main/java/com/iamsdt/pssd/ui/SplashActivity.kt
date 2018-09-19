/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:41 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.BuildConfig
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.runThread
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.main.MainActivity
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.sync.worker.DataInsertWorker
import com.iamsdt.pssd.utils.sync.worker.DownloadWorker
import com.iamsdt.pssd.utils.sync.worker.UploadWorker
import org.koin.android.ext.android.inject
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val spUtils: SpUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_splash)

        //save data

        // TODO: 8/31/18 add animation

        if (!spUtils.isDatabaseInserted) {
            //save database
            val request = OneTimeWorkRequest.Builder(
                    DataInsertWorker::class.java).build()
            WorkManager.getInstance().beginUniqueWork("DataInsert",
                    ExistingWorkPolicy.REPLACE, request).enqueue()

            //save app start date
            saveAppStartDate()
        }

        val time = if (BuildConfig.DEBUG) 100L
        else 1000L
        val next = MainActivity::class

        runThread(time, next)

        //put data on analytics
        val ana = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("app_open", "App open")
        ana.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)


    }

    //complete:8/24/18 Debug only remove latter
    //send current date
    private fun saveAppStartDate() {
        val date = Date()
        spUtils.saveDownloadDate(date.time)
        spUtils.saveUploadDate(date.time)
    }

    private fun fakeUpload() {
        val request = OneTimeWorkRequest
                .Builder(UploadWorker::class.java).build()
        WorkManager.getInstance().beginUniqueWork("Upload",
                ExistingWorkPolicy.REPLACE, request).enqueue()
    }

    private fun fakeDownload() {
        val request = OneTimeWorkRequest
                .Builder(DownloadWorker::class.java)
                .addTag(Constants.REMOTE.DOWNLOAD_TAG)
                .build()
        WorkManager.getInstance().beginUniqueWork("Download",
                ExistingWorkPolicy.REPLACE, request).enqueue()
    }
}
