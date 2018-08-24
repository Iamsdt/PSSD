/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 4:54 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iamsdt.pssd.database.MyDatabase
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ui.add.AddVM
import com.iamsdt.pssd.ui.details.DetailsVM
import com.iamsdt.pssd.ui.favourite.FavouriteAdapter
import com.iamsdt.pssd.ui.favourite.FavouriteVM
import com.iamsdt.pssd.ui.flash.FlashVM
import com.iamsdt.pssd.ui.main.MainVM
import com.iamsdt.pssd.ui.search.SearchVM
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.FileImportExportUtils
import com.iamsdt.pssd.utils.SettingsUtils
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.sync.SyncTask
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dbModule = module {

    single { get<MyDatabase>().wordTableDao }

    single {
        Room.databaseBuilder(androidContext(),
                MyDatabase::class.java, Constants.DB_NAME).build()
    }
}

val appModule = module {
    single {
        SpUtils(androidContext())
    }

    single {
        SettingsUtils(androidContext())
    }

    single {
        GsonBuilder().create()
    }

    single {
        FileImportExportUtils(get() as WordTableDao,
                get() as SettingsUtils,
                get() as Gson)
    }

    single {
        FavouriteAdapter(
                androidContext(),
                get() as WordTableDao)
    }

    single {
        SyncTask(get(), get(), get(), get())
    }
}

val vmModule = module {

    viewModel { MainVM(get()) }
    viewModel { DetailsVM(get()) }
    viewModel { FavouriteVM(get()) }
    viewModel { FlashVM(get()) }
    viewModel { SearchVM(get()) }
    viewModel { AddVM(get()) }
}