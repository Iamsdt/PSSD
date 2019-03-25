/*
 * Developed By Shudipto Trafder
 * on 8/17/18 5:48 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.details

import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.ScopeViewModel
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Bookmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DetailsVM(val wordTableDao: WordTableDao) : ScopeViewModel() {

    //track bookmark
    private val singleLiveEvent = SingleLiveEvent<Bookmark>()

    //get single word
    fun getWord(id: Int) = wordTableDao.getSingleWord(id)

    private fun setBookmark(id: Int) {
        uiScope.launch(Dispatchers.IO) {
            val update = wordTableDao.setBookmark(id)
            if (update > 0)
                singleLiveEvent.postValue(Bookmark.SET)
        }
    }

    private fun setRecent(id: Int){
        uiScope.launch(Dispatchers.IO) {
            val date = Date().time
            wordTableDao.setRecent(id,date)
        }
    }

    private fun deleteBookmark(id: Int) {
        uiScope.launch(Dispatchers.IO) {
            val delete = wordTableDao.deleteBookmark(id)
            if (delete > 0)
                singleLiveEvent.postValue(Bookmark.DELETE)
        }
    }

    fun requestBookmark(id: Int, bookmarked: Boolean) {
        if (bookmarked)
            deleteBookmark(id)
        else
            setBookmark(id)
    }

}
