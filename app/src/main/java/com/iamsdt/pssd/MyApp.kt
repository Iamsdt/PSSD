/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:41 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.crashlytics.android.Crashlytics
import com.iamsdt.pssd.di.appModule
import com.iamsdt.pssd.di.dbModule
import com.iamsdt.pssd.di.vmModule
import com.iamsdt.pssd.ext.DebugLogTree
import com.iamsdt.pssd.ext.ReleaseLogTree
import com.rohitss.uceh.UCEHandler
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(applicationContext, Crashlytics())

        if (BuildConfig.DEBUG) {
            UCEHandler.Builder(applicationContext).build()
            Timber.plant(DebugLogTree())
        }

        startKoin(this, listOf(dbModule, appModule, vmModule))

        createNotificationChannel()
    }

    //create notification channel
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val description = getString(R.string.noti_channel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(packageName, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }

}