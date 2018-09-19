/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 4:58 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.sync.worker

import androidx.work.Worker
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.model.JsonModel
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.io.InputStreamReader

class DataInsertWorker : Worker(), KoinComponent {

    private val gson: Gson by inject()

    private val wordTableDao: WordTableDao by inject()

    private val spUtils: SpUtils by inject()

    override fun doWork(): Result {

        var result = Result.SUCCESS

        val stream = applicationContext.assets.open("data.json")

        val reader = InputStreamReader(stream)

        val data = gson.fromJson(
                reader.buffered(4096),
                JsonModel::class.java)

        var count = 0L

        data?.let { model ->
            spUtils.dataVolume = model.volume
            model.collection.forEach {
                val wordTable = WordTable(word = it.word, des = it.des)
                count = wordTableDao.add(wordTable)
            }
        }

        if (count > 0) {
            spUtils.isDatabaseInserted = true
        } else {
            result = Result.FAILURE
        }

        Timber.i("Total added: $count")

        return result
    }

}