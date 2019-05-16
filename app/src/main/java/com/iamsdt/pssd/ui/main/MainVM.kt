/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:55 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.ScopeViewModel
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Bookmark
import com.iamsdt.pssd.utils.PAGE_CONFIG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class MainVM(val wordTableDao: WordTableDao) : ScopeViewModel() {

    val searchEvent = SingleLiveEvent<WordTable>()

    val singleWord = SingleLiveEvent<WordTable>()

    private val queryLiveData = MutableLiveData<String>()

    val myLiveData: LiveData<PagedList<WordTable>> =
            Transformations.switchMap(queryLiveData, ::temp)

    private fun temp(string: String = ""): LiveData<PagedList<WordTable>> {
        val source = wordTableDao.getSearchData(string)
        return LivePagedListBuilder(source, PAGE_CONFIG)
                .build()
    }

    fun submitQuery(query: String = "") {
        queryLiveData.value = query
    }

    fun getSingleWord(id: Int) {
        uiScope.launch(Dispatchers.IO) {
            val word = wordTableDao.getWordByID(id)
            singleWord.postValue(word)
        }
    }

    fun submit(query: String?) {

        if (query?.isEmpty() == true) {
            return
        }

        query?.let {
            //make first latter capital
            uiScope.launch {
                val w = it.replaceFirst(it[0], it[0].toUpperCase(), false)
                val word: WordTable? = withContext(Dispatchers.IO) { wordTableDao.getSearchResult(w) }
                Timber.i("Word:$word")
                //complete 10/23/2018 fix latter
                searchEvent.postValue(word)
            }
        }
    }

    //track bookmark
    val singleLiveEvent = SingleLiveEvent<Bookmark>()

    //get single word
    fun getWord(id: Int) = wordTableDao.getSingleWord(id)

    private fun setBookmark(id: Int) {
        uiScope.launch {
            val update = withContext(Dispatchers.IO) {
                wordTableDao.setBookmark(id)
            }
            if (update > 0)
                singleLiveEvent.postValue(Bookmark.SET)
        }
    }

    private fun deleteBookmark(id: Int) {
        uiScope.launch {
            val delete = withContext(Dispatchers.IO) {
                wordTableDao.deleteBookmark(id)
            }
            if (delete > 0)
                singleLiveEvent.postValue(Bookmark.DELETE)
        }
    }

    fun setRecent(id: Int) {
        val date = Date().time
        uiScope.launch(Dispatchers.IO) {
            wordTableDao.setRecent(id, date)
        }
    }

    fun requestBookmark(id: Int, bookmarked: Boolean) {
        if (bookmarked)
            deleteBookmark(id)
        else
            setBookmark(id)
    }

}