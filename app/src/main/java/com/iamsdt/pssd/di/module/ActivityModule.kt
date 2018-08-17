/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:34 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.di.module

import com.iamsdt.pssd.ui.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

}
