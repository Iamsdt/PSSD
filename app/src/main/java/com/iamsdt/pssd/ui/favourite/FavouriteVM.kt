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
import com.iamsdt.pssd.utils.PAGE_CONFIG

class FavouriteVM(
        val wordTableDao: WordTableDao) : ViewModel() {

    fun getData(): LiveData<PagedList<WordTable>> {
        val source = wordTableDao.getBookmarkData()
        return LivePagedListBuilder(source, PAGE_CONFIG).build()
    }


}