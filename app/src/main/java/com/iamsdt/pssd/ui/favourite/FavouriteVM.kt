/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 6:26 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao

class FavouriteVM(
        val wordTableDao: WordTableDao) : ViewModel() {

    fun getData(): LiveData<PagedList<WordTable>> {

        val source = wordTableDao.getBookmarkData()

        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)//by default page list * 3
                .setPrefetchDistance(10) // default page list
                .setEnablePlaceholders(true) //default true
                // that's means scroll bar is not jump and all data set show on the
                //recycler view first after 30 it will show empty view
                // when load it will update with animation
                .build()


        return LivePagedListBuilder(source, config).build()
    }


}