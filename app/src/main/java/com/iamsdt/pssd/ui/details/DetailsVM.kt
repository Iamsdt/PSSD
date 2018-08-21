/*
 * Developed By Shudipto Trafder
 * on 8/17/18 5:48 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.details

import android.os.AsyncTask
import androidx.lifecycle.ViewModel
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Bookmark
import javax.inject.Inject

class DetailsVM @Inject constructor(val wordTableDao: WordTableDao):
        ViewModel(){

    val singleLiveEvent = SingleLiveEvent<Bookmark>()

    fun getWord(id:Int) = wordTableDao.getSingleWord(id)

    private fun setBookmark(id: Int) {
        AsyncTask.execute {
            val update = wordTableDao.setBookmark(id)
            if (update != -1)
                singleLiveEvent.postValue(Bookmark.SET)
        }
    }

    private fun deleteBookmark(id: Int) {
        AsyncTask.execute {
            val delete = wordTableDao.deleteBookmark(id)
            if (delete != -1)
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
