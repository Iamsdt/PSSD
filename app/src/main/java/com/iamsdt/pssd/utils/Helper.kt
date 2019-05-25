/*
 * Developed By Shudipto Trafder
 *  on 9/18/18 11:36 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.os.AsyncTask
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.utils.model.Model
import org.joda.time.DateTime
import org.joda.time.Days
import timber.log.Timber
import java.util.*

val PAGE_CONFIG: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(30)
        .setInitialLoadSizeHint(50)//by default page size * 3
        .setPrefetchDistance(20) // default page size
        .setEnablePlaceholders(true) //default true
        .build()


//background thread
fun ioThread(f: () -> Unit) {
    AsyncTask.execute(f)
}

fun myThread(f:() -> Unit){
    AsyncTask.execute(f)
}

fun Model.toWordTable() = WordTable(word = word, des = des, reference = ref)

//return interval date
fun getDayInterval(oldDate: Long): Int {

    val today = DateTime(Date())
    val preDate = DateTime(oldDate)

    val day = Days.daysBetween(preDate, today).days

    Timber.i(day.toString())

    return day
}