/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:34 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.di.module

import com.iamsdt.pssd.ui.SplashActivity
import com.iamsdt.pssd.ui.details.DetailsActivity
import com.iamsdt.pssd.ui.main.MainActivity
import com.iamsdt.pssd.ui.service.DataInsertService
import com.iamsdt.pssd.ui.settings.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ContributesAndroidInjector
    abstract fun main(): MainActivity

    @ContributesAndroidInjector
    abstract fun details(): DetailsActivity

    @ContributesAndroidInjector
    abstract fun dataInsert(): DataInsertService

    @ContributesAndroidInjector
    abstract fun settings(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun settingsAdvance(): AdvanceSettings

    @ContributesAndroidInjector
    abstract fun settingsBackup(): BackupSettings

    @ContributesAndroidInjector
    abstract fun settingsFragBackup(): BackupFragment

    @ContributesAndroidInjector
    abstract fun settingsFragAdvance(): AdvanceFragment

    @ContributesAndroidInjector
    abstract fun settingsFrag(): SettingsFragment

}
