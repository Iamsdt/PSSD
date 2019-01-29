package com.iamsdt.pssd.database

import androidx.room.TypeConverter
import java.util.*

class Converters{

    @TypeConverter
    fun toDate(date: Long) = Date(date)

    @TypeConverter
    fun toLong(date: Date) = date.time

}