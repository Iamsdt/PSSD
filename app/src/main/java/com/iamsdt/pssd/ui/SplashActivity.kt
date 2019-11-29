/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:41 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.androidextension.runThreadK
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ui.color.ThemeUtils
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
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_splash)

        // complete: 8/31/18 add animation
        val set = AnimationUtils.loadAnimation(this,
                R.anim.splash_screen_animation)

        app_icon.animation = set
        textView32.animation = set

        if (!spUtils.isDatabaseInserted) {
            //save database
            val data = DataInsertWorker(this)
            data.doWork()
            data.status.observe(this, androidx.lifecycle.Observer {
                if (it) runThreadK<MainActivity>(500L)
            })

            //save app start date
            saveAppStartDate()
            main_splash.showSnackbar("Preparing database...\nIt will take awhile...",Snackbar.LENGTH_INDEFINITE)

        } else {
            runThreadK<MainActivity>(1000L)
        }

        //complete change to 1s
        if (spUtils.isDatabaseInserted && spUtils.isUpdateRequestForVersion4) {
            val request = OneTimeWorkRequest.Builder(
                    UpdateWorker::class.java)
                    .setInitialDelay(1, TimeUnit.SECONDS)
                    .build()
            WorkManager.getInstance().beginUniqueWork("DataInsert",
                    ExistingWorkPolicy.KEEP, request).enqueue()
        }

        //put data on analytics
        val ana = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("app_open", "App open")
        ana.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

    }

    private fun View.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(this, snackbarText, timeLength).show()
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
