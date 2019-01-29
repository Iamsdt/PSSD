/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:41 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.runThread
import com.iamsdt.pssd.ui.main.MainActivity
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.sync.worker.DataInsertWorker
import com.iamsdt.pssd.utils.sync.worker.DownloadWorker
import com.iamsdt.pssd.utils.sync.worker.UpdateWorker
import com.iamsdt.pssd.utils.sync.worker.UploadWorker
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject
import java.util.*
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private val spUtils: SpUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // complete: 8/31/18 add animation
        val set = AnimationUtils.loadAnimation(this,
                R.anim.splash_screen_animation)

        app_icon.animation = set

        if (!spUtils.isDatabaseInserted) {
            //save database
            val request = OneTimeWorkRequest.Builder(
                    DataInsertWorker::class.java).build()
            WorkManager.getInstance().beginUniqueWork("DataInsert",
                    ExistingWorkPolicy.KEEP, request).enqueue()

            //save app start date
            saveAppStartDate()
        }

        //todo change to 5s
        if (spUtils.isUpdateRequestForVersion4) {
            val request = OneTimeWorkRequest.Builder(
                    UpdateWorker::class.java)
                    .setInitialDelay(10, TimeUnit.SECONDS).build()
            WorkManager.getInstance().beginUniqueWork("DataInsert",
                    ExistingWorkPolicy.KEEP, request).enqueue()
        }

        val next = MainActivity::class

        runThread(1000L, next)

        //put data on analytics
        val ana = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("app_open", "App open")
        ana.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)


    }

    //complete:8/24/18 only remove latter
    //send current date
    private fun saveAppStartDate() {
        val date = Date()
        spUtils.downloadDate = date.time
        spUtils.uploadDate = date.time
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
