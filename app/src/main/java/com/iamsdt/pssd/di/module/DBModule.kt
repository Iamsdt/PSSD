/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:37 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

/*
 * Created by Shudipto Trafder
 * on 6/9/18 5:15 PM
 */

package com.iamsdt.pssd.di.module

import android.app.Application
import androidx.room.Room
import com.iamsdt.pssd.database.MyDatabase
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.Constants.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule{

    @Provides
    @Singleton
    fun getPostDao(myDB: MyDatabase):WordTableDao =
            myDB.wordTableDao


    @Provides
    @Singleton
    fun getDb(context:Application):MyDatabase = Room.databaseBuilder(context,
            MyDatabase::class.java, DB_NAME).build()
}