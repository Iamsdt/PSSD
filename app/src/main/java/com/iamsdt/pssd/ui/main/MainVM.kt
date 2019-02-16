/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:55 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import androidx.lifecycle.MediatorLiveData
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

class MainVM(val wordTableDao: WordTableDao) : ScopeViewModel() {

    val searchEvent = SingleLiveEvent<WordTable>()

    val singleWord = SingleLiveEvent<WordTable>()

    lateinit var liveData: MediatorLiveData<PagedList<WordTable>>

    init {
        if (!::liveData.isInitialized) {
            liveData = MediatorLiveData()
            getData()
        }
    }

    fun getSingleWord(id: Int) {
        uiScope.launch(Dispatchers.Main) {
            val word = withContext(Dispatchers.IO) {
                wordTableDao.getWordByID(id)
            }
            singleWord.postValue(word)
        }
    }

    private fun getData() {

        val source = wordTableDao.getAllData()

        val date = LivePagedListBuilder(source, PAGE_CONFIG)
                .build()

        liveData.addSource(date) {
            liveData.value = it
        }
    }

    fun requestSearch(query: String) {

        val source =
                wordTableDao.getSearchData(query)


        val data = LivePagedListBuilder(source, PAGE_CONFIG).build()

        liveData.addSource(data) {
            if (liveData.value != it) {
                //prevent multiple update
                liveData.value = it
            }
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

    fun requestBookmark(id: Int, bookmarked: Boolean) {
        if (bookmarked)
            deleteBookmark(id)
        else
            setBookmark(id)
    }

}