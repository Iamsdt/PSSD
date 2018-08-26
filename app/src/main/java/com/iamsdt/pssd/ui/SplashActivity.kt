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
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.sync.worker.DataInsertWorker
import org.joda.time.DateTime
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    val spUtils: SpUtils by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_splash)

        //save data

        val next = MainActivity::class

        if (!spUtils.isDatabaseInserted) {
            //save database
            val request = OneTimeWorkRequest.Builder(
                    DataInsertWorker::class.java).build()
            WorkManager.getInstance().beginUniqueWork("DataInsert",
                    ExistingWorkPolicy.REPLACE, request).enqueue()
        }

        val time = if (BuildConfig.DEBUG) 100L
        else 1000L

        runThread(time, next)

        //put data on analytics
        val ana = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("app_open","App open")
        ana.logEvent(FirebaseAnalytics.Event.APP_OPEN,bundle)
    }

    //debugOnly:8/24/18 Debug only remove latter
    //send current date
    fun saveAppStartDate() {
        val date = DateTime(2018, 8, 17, 0, 0)
        spUtils.saveDownloadDate(date.toDate().time)
        spUtils.saveUploadDate(date.toDate().time)
    }
}
