/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:55 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.os.AsyncTask
import android.provider.SearchRecentSuggestions
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.model.StatusModel
import timber.log.Timber
import java.util.*

class MainVM(val wordTableDao: WordTableDao) : ViewModel() {

    val event = SingleLiveEvent<StatusModel>()

    lateinit var liveData: MediatorLiveData<PagedList<WordTable>>

    private val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(20)//by default page size * 3
            .setPrefetchDistance(10) // default page size
            .setEnablePlaceholders(false) //default true
            .build()

    init {
        if (!::liveData.isInitialized) {
            liveData = MediatorLiveData()
            getData()
        }
    }

    private fun getData() {

        val source = wordTableDao.getAllData()

        val date = LivePagedListBuilder(source, config)
                .build()

        liveData.addSource(date) {
            liveData.value = it
        }
    }

    fun getRandomWord(): LiveData<WordTable>? {
        var liveData:LiveData<WordTable> ?= null
        AsyncTask.execute {
            val size = wordTableDao.getAllList().size
            val random = Random()
            val id = random.nextInt(size)
            liveData = wordTableDao.getSingleWord(id)
        }
        return liveData
    }

    fun requestSearch(query: String) {


        val source = wordTableDao.getSearchData(query)

        val data = LivePagedListBuilder(source, config).build()

        liveData.addSource(data) {
            if (liveData.value != it) {
                //prevent multiple update
                liveData.value = it
            }
        }
    }

    val randomData get() = wordTableDao.getRandomData()


    fun submit(query: String?, suggestions: SearchRecentSuggestions?) {
        query?.let {
            AsyncTask.execute {
                val word: WordTable? = wordTableDao.getSearchResult(it)
                Timber.i("Word:$word")
                if (word != null) {
                    suggestions?.saveRecentQuery(query, null)
                    event.postValue(StatusModel(true, Constants.SEARCH, "${word.id}"))
                } else {
                    event.postValue(StatusModel(false, Constants.SEARCH, query))
                }
            }
        }
    }

}