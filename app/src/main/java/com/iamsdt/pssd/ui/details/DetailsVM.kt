/*
 * Developed By Shudipto Trafder
 * on 8/17/18 5:48 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.details

import androidx.lifecycle.ViewModel
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Bookmark
import com.iamsdt.pssd.utils.ioThread

class DetailsVM(val wordTableDao: WordTableDao) : ViewModel() {

    //track bookmark
    private val singleLiveEvent = SingleLiveEvent<Bookmark>()

    //get single word
    fun getWord(id: Int) = wordTableDao.getSingleWord(id)

    private fun setBookmark(id: Int) {
        ioThread {
            val update = wordTableDao.setBookmark(id)
            if (update > 0)
                singleLiveEvent.postValue(Bookmark.SET)
        }
    }

    private fun deleteBookmark(id: Int) {
        ioThread {
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
