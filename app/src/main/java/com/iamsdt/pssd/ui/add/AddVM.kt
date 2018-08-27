/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 9:01 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.add

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Constants.ADD.DES
import com.iamsdt.pssd.utils.Constants.ADD.DIALOG
import com.iamsdt.pssd.utils.Constants.ADD.WORD
import com.iamsdt.pssd.utils.model.StatusModel

class AddVM(private val wordTableDao: WordTableDao) : ViewModel() {

    val dialogStatus = SingleLiveEvent<StatusModel>()

    fun addData(word: String, des: String) {

        if (word.isEmpty() || word.length <= 2) {
            dialogStatus.value = StatusModel(false,
                    WORD, "Please input correct word")

            return
        }

        if (des.isEmpty() || des.length <= 2) {
            dialogStatus.value = StatusModel(false,
                    DES, "Please input correct description")

            return
        }


        val wordTable = WordTable(word = word, des = des, addByUser = true)

        AsyncTask.execute {
            val status = wordTableDao.add(wordTable)
            if (status >= 0) {
                dialogStatus.postValue(StatusModel(true, DIALOG,
                        "Word added successfully"))
            }
        }

    }

    fun getWord(): LiveData<PagedList<WordTable>> {
        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)//by default page list * 3
                .setPrefetchDistance(10) // default page list
                .setEnablePlaceholders(false) //default true
                .build()

        val source = wordTableDao.getAddedWordByUser()

        return LivePagedListBuilder(source, config)
                .build()
    }

}