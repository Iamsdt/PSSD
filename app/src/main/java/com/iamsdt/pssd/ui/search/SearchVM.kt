/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 10:23 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import javax.inject.Inject

class SearchVM @Inject constructor(val wordTableDao: WordTableDao):ViewModel(){

    var data = MediatorLiveData<PagedList<WordTable>>()

    fun requestSearch(query: String) {
        val source = wordTableDao.getSearchData(query)

        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)//by default page size * 3
                .setPrefetchDistance(10) // default page size
                .setEnablePlaceholders(true) //default true
                .build()


        val liveData = LivePagedListBuilder(source, config).build()

        //change the data
        data.addSource(liveData) {}
    }

}
