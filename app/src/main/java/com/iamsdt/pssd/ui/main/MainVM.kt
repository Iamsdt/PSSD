/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:55 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.PAGE_CONFIG
import com.iamsdt.pssd.utils.ioThread
import com.iamsdt.pssd.utils.model.StatusModel
import timber.log.Timber

class MainVM(val wordTableDao: WordTableDao) : ViewModel() {

    val event = SingleLiveEvent<StatusModel>()

    lateinit var liveData: MediatorLiveData<PagedList<WordTable>>

    init {
        if (!::liveData.isInitialized) {
            liveData = MediatorLiveData()
            getData()
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

//    fun getRandomWord(): LiveData<WordTable>? {
//        var liveData: LiveData<WordTable>? = null
//        AsyncTask.execute {
//            val size = wordTableDao.getAllList().size
//            val random = Random()
//            val id = random.nextInt(size)
//            liveData = wordTableDao.getSingleWord(id)
//        }
//        return liveData
//    }

    fun requestSearch(query: String) {

        val source = wordTableDao.getSearchData(query)

        val data = LivePagedListBuilder(source, PAGE_CONFIG).build()

        liveData.addSource(data) {
            if (liveData.value != it) {
                //prevent multiple update
                liveData.value = it
            }
        }
    }


    fun submit(query: String?) {
        query?.let {
            ioThread {
                val word: WordTable? = wordTableDao.getSearchResult(it)
                Timber.i("Word:$word")
                if (word != null) {
                    event.postValue(StatusModel(true, Constants.SEARCH,
                            "${word.id}", extra = query))
                } else {
                    event.postValue(StatusModel(false, Constants.SEARCH,
                            "Word not found", extra = query))
                }
            }
        }
    }

}