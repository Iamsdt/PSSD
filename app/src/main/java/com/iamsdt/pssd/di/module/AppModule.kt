/*
 * Developed By Shudipto Trafder
 * on 8/17/18 4:11 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.di.module

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.FileImportExportUtils
import com.iamsdt.pssd.utils.SettingsUtils
import com.iamsdt.pssd.utils.SpUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {


    @Provides
    @Singleton
    fun getFileExport(dao: WordTableDao,
                      utils: SettingsUtils,
                      gson: Gson): FileImportExportUtils =
            FileImportExportUtils(dao, utils, gson)

    @Provides
    @Singleton
    fun gson() = GsonBuilder().create()

    @Provides
    @Singleton
    fun getSettingsSp(app: Application): SettingsUtils =
            SettingsUtils(app)

    @Provides
    @Singleton
    fun getSp(app: Application) = SpUtils(app.baseContext)

}