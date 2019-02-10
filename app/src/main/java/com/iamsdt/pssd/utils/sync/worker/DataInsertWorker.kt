/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 4:58 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.sync.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.toWordTable
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.model.JsonModel
import kotlinx.coroutines.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.io.InputStreamReader

class DataInsertWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters), KoinComponent {

    private val gson: Gson by inject()

    private val wordTableDao: WordTableDao by inject()

    private val spUtils: SpUtils by inject()

    private val bgScope = CoroutineScope(Dispatchers.IO)

    override fun doWork(): Result {

        var result = Result.success()

        bgScope.launch {

            val stream = async(Dispatchers.Main) {
                applicationContext.assets.open("data.json")
            }

            val reader = InputStreamReader(stream.await())

            val data =
                    gson.fromJson(
                            reader.buffered(4096),
                            JsonModel::class.java)

            var count = 0L

            data.let { model ->
                model.collection.filter {
                    it.word.isNotEmpty()
                }.forEach {
                    count = wordTableDao.add(it.toWordTable())
                }

                //save version
                spUtils.dataVolume = model.volume
            }

            if (count > 0) {
                spUtils.isDatabaseInserted = true

                if (spUtils.isUpdateRequestForVersion4) {
                    spUtils.isUpdateRequestForVersion4 = true
                }

            } else result = Result.failure()

            Timber.i("Total added: $count")

        }
        return result
    }

    override fun onStopped() {
        super.onStopped()
        bgScope.coroutineContext.cancelChildren()
    }

}