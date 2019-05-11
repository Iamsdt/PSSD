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
import com.iamsdt.pssd.ui.add.AddAdapter
import com.iamsdt.pssd.ui.add.AddVM
import com.iamsdt.pssd.ui.details.DetailsVM
import com.iamsdt.pssd.ui.favourite.FavouriteAdapter
import com.iamsdt.pssd.ui.favourite.FavouriteVM
import com.iamsdt.pssd.ui.flash.FlashVM
import com.iamsdt.pssd.ui.main.MainVM
import com.iamsdt.pssd.utils.*
import com.iamsdt.pssd.utils.sync.SyncTask
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dbModule = module {

    single { get<MyDatabase>().wordTableDao }

    single {
        Room.databaseBuilder(androidContext(),
                MyDatabase::class.java, Constants.DB_NAME)
                .addMigrations(DbHelper.migration_1_2, DbHelper.migration_2_3, DbHelper.migration_3_4)
                .enableMultiInstanceInvalidation()
                .build()
    }
}

val appModule = module {

    single {
        RestoreData(get(), get(), get(), get())
    }

    single {
        TxtHelper(get() as SettingsUtils)
    }

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
        AddAdapter(
                androidContext(),
                get() as WordTableDao)
    }

    single {
        SyncTask(get(), get(), get())
    }
}

val vmModule = module {
    viewModel { MainVM(get()) }
    viewModel { DetailsVM(get()) }
    viewModel { FavouriteVM(get()) }
    viewModel { FlashVM(get()) }
    viewModel { AddVM(get()) }
}