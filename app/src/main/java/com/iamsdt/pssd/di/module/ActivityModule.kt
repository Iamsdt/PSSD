/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:34 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.di.module

import com.iamsdt.pssd.ui.SplashActivity
import com.iamsdt.pssd.ui.details.DetailsActivity
import com.iamsdt.pssd.ui.favourite.FavouriteActivity
import com.iamsdt.pssd.ui.flash.FlashCardActivity
import com.iamsdt.pssd.ui.flash.FlashSheet
import com.iamsdt.pssd.ui.main.MainActivity
import com.iamsdt.pssd.ui.main.RandomDialog
import com.iamsdt.pssd.ui.search.SearchActivity
import com.iamsdt.pssd.ui.service.DataInsertService
import com.iamsdt.pssd.ui.settings.AdvanceFragment
import com.iamsdt.pssd.ui.settings.AdvanceSettings
import com.iamsdt.pssd.ui.settings.BackupFragment
import com.iamsdt.pssd.ui.settings.BackupSettings
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun splash(): SplashActivity

    @ContributesAndroidInjector
    abstract fun main(): MainActivity

    @ContributesAndroidInjector
    abstract fun flashSheet(): FlashSheet

    @ContributesAndroidInjector
    abstract fun random(): RandomDialog

    @ContributesAndroidInjector
    abstract fun details(): DetailsActivity

    @ContributesAndroidInjector
    abstract fun dataInsert(): DataInsertService

    @ContributesAndroidInjector
    abstract fun settingsAdvance(): AdvanceSettings

    @ContributesAndroidInjector
    abstract fun settingsBackup(): BackupSettings

    @ContributesAndroidInjector
    abstract fun settingsFragBackup(): BackupFragment

    @ContributesAndroidInjector
    abstract fun settingsFragAdvance(): AdvanceFragment

    @ContributesAndroidInjector
    abstract fun favSettings(): FavouriteActivity

    @ContributesAndroidInjector
    abstract fun flash(): FlashCardActivity

    @ContributesAndroidInjector
    abstract fun search(): SearchActivity
}
