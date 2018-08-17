/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:41 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iamsdt.pssd.BuildConfig
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.runThread
import com.iamsdt.pssd.ui.main.MainActivity
import com.iamsdt.pssd.ui.service.DataInsertService
import com.iamsdt.pssd.utils.SpUtils
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var spUtils: SpUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //save data

        val next = MainActivity::class

        if (!spUtils.isDatabaseInserted) {
            //save database
            startService(Intent(this,DataInsertService::class.java))
        }

        val time = if (BuildConfig.DEBUG) 100L
        else 1000L

        runThread(time,next)

    }
}
