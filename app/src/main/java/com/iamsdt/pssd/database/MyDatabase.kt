/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:39 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(value = [Converters::class])
@Database(entities = [WordTable::class], version = 5,
        exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract val wordTableDao: WordTableDao
}