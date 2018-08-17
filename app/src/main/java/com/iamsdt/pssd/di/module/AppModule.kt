/*
 * Developed By Shudipto Trafder
 * on 8/17/18 4:11 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.di.module

import android.app.Application
import com.iamsdt.pssd.utils.SpUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule{

    @Provides
    @Singleton
    fun getSp(app:Application)= SpUtils(app.baseContext)

}