/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 4:58 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.sync.worker

import android.content.Context
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.ext.toWordTable
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.model.JsonModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.io.InputStreamReader

class DataInsertWorker(val context: Context) : KoinComponent {

    private val gson: Gson by inject()

    private val wordTableDao: WordTableDao by inject()

    private val spUtils: SpUtils by inject()

    private val bgScope = CoroutineScope(Dispatchers.IO)

    val status = SingleLiveEvent<Boolean>()

    fun doWork() {

        bgScope.launch(Dispatchers.IO) {

            val stream = withContext(Dispatchers.Main) {
                context.assets.open("soil_database.json")
            }

            val reader = InputStreamReader(stream)

            val data =
                    gson.fromJson(
                            reader.buffered(4096),
                            JsonModel::class.java)

            var count = 0L

            data.let { model ->
                model.collection.filter {
                    it.word.isNotEmpty()
                }.forEach {
                    count = withContext(Dispatchers.IO) {
                        wordTableDao.add(it.toWordTable())
                    }
                }

                //save version
                spUtils.dataVolume = model.volume
            }

            if (count > 0) {
                spUtils.isDatabaseInserted = true

                status.postValue(true)

                if (spUtils.isUpdateRequestForVersion4) {
                    spUtils.isUpdateRequestForVersion4 = false
                }

            }

            Timber.i("Total added: $count")
        }
    }
}