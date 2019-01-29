package com.iamsdt.pssd.utils.sync.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.toWordTable
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.model.JsonModel
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.io.InputStreamReader

class UpdateWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters), KoinComponent {

    private val gson: Gson by inject()

    private val wordTableDao: WordTableDao by inject()

    private val spUtils: SpUtils by inject()

    override fun doWork(): Result {

        var result = Result.success()

        val stream = applicationContext.assets.open("data.json")

        val reader = InputStreamReader(stream)

        val data = gson.fromJson(
                reader.buffered(4096),
                JsonModel::class.java)

        var count = 0

        data?.let { model ->
            model.collection.filter {
                it.word.isNotEmpty()
            }.forEach {
                var table: WordTable? = wordTableDao.getWord(it.word)

                table = table?.copy(reference = it.ref) ?: it.toWordTable()

                count = wordTableDao.update(table)
            }

            //save version
            spUtils.dataVolume = model.volume
        }

        if (count > 0) {
            spUtils.isUpdateRequestForVersion4 = true

        } else result = Result.failure()

        Timber.i("Total added: $count")

        return result
    }

}