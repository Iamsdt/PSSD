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

class DetailsVM (val wordTableDao: WordTableDao):
        ViewModel(){

    val singleLiveEvent = SingleLiveEvent<Bookmark>()

    fun getWord(word:String) = wordTableDao.getSingleWord(word)

    private fun setBookmark(word: String) {
        AsyncTask.execute {
            val update = wordTableDao.setBookmark(word)
            if (update != -1)
                singleLiveEvent.postValue(Bookmark.SET)
        }
    }

    private fun deleteBookmark(word: String) {
        AsyncTask.execute {
            val delete = wordTableDao.deleteBookmark(word)
            if (delete != -1)
                singleLiveEvent.postValue(Bookmark.DELETE)
        }
    }

    fun requestBookmark(word: String, bookmarked: Boolean) {
        if (bookmarked)
            deleteBookmark(word)
        else
            setBookmark(word)
    }

}
