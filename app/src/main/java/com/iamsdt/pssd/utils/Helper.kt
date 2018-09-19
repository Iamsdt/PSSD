/*
 * Developed By Shudipto Trafder
 *  on 9/18/18 11:36 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.os.AsyncTask
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

val PAGE_CONFIG: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(10)
        .setInitialLoadSizeHint(20)//by default page size * 3
        .setPrefetchDistance(10) // default page size
        .setEnablePlaceholders(false) //default true
        .build()

fun ioThread(f: () -> Unit) {
    AsyncTask.execute(f)
}