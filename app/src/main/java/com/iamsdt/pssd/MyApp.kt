/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:41 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd

import android.app.Activity
import android.os.Bundle
import com.iamsdt.pssd.di.AppComponent
import com.iamsdt.pssd.di.DaggerAppComponent
import com.iamsdt.pssd.ext.DebugLogTree
import com.iamsdt.pssd.ext.LifeCycle
import com.iamsdt.pssd.ext.ReleaseLogTree
import com.rohitss.uceh.UCEHandler
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class MyApp:DaggerApplication(){

    private val component: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>
            = component


    override fun onCreate() {
        super.onCreate()

        component.inject(this)

        UCEHandler.Builder(applicationContext).build()

        if (BuildConfig.DEBUG) Timber.plant(DebugLogTree())
        else Timber.plant(ReleaseLogTree())

        registerActivityLifecycleCallbacks(object : LifeCycle() {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                super.onActivityCreated(activity, savedInstanceState)
                activity?.let {
                    //try to inject in the activity
                    //if not found catch the exception
                    try {
                        AndroidInjection.inject(it)
                    }catch (e:Exception){
                        //Timber.d(e,"Inject error")
                    }
                }
            }
        })
    }

}