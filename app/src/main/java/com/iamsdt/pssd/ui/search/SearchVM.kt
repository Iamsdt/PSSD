/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 10:23 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.search

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.Constants.Companion.SEARCH
import com.iamsdt.pssd.utils.model.StatusModel
import timber.log.Timber
import javax.inject.Inject

class SearchVM @Inject constructor(val wordTableDao: WordTableDao) : ViewModel() {

    val event = SingleLiveEvent<StatusModel>()

    fun getData(query: String):LiveData<PagedList<WordTable>>{

        val source = wordTableDao.getSearchData(query)

        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)//by default page size * 3
                .setPrefetchDistance(10) // default page size
                .setEnablePlaceholders(true) //default true
                .build()


        return LivePagedListBuilder(source, config).build()
    }

    fun requestSearch(query: String){
        AsyncTask.execute {
            event.postValue(StatusModel(true, Constants.SEARCH_DATA,query))
        }
    }

    fun submit(query: String?) {
        query?.let {
            AsyncTask.execute {
                val word: WordTable? = wordTableDao.getSearchResult(it)
                Timber.i("Word:$word")
                if (word != null) {
                    event.postValue(StatusModel(true, SEARCH, "${word.id}"))
                } else {
                    event.postValue(StatusModel(false, SEARCH, "Word not found!"))
                }
            }
        }
    }

}
