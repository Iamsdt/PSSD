/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:39 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordTable::class], version = 1,
        exportSchema = false)
abstract class MyDatabase:RoomDatabase(){
    abstract val wordTableDao: WordTableDao
}