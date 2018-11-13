/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 4:57 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.sync.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.toWordTable
import com.iamsdt.pssd.ui.main.MainActivity
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.Constants.REMOTE.DOWNLOAD_FILE_NAME
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.ioThread
import com.iamsdt.pssd.utils.model.RemoteModel
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.io.File
import java.util.*

class DownloadWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters), KoinComponent {

    private val gson: Gson by inject()

    private val wordTableDao: WordTableDao by inject()

    private val spUtils: SpUtils by inject()

    override fun doWork(): Result {

        Timber.i("Download worker begin")

        var result = Result.FAILURE

        val auth = FirebaseAuth.getInstance()

        val user = auth?.currentUser

        if (user == null) {
            //sign in
            auth.signInAnonymously().addOnCompleteListener { task ->
                result = if (task.isSuccessful) {
                    //write in  the database
                    writeDB()

                } else Result.RETRY
            }
        } else {
            result = writeDB()
        }

        return result
    }

    private fun writeDB(): Result {

        var result = Result.FAILURE

        val db = FirebaseStorage.getInstance()
        val ref = db.reference
                .child(Constants.REMOTE.ADMIN)
                .child(DOWNLOAD_FILE_NAME)

        val file = File.createTempFile("download", ".json")

        ref.getFile(file).addOnSuccessListener {

            Timber.i("Data found")

            val data = gson.fromJson(file.bufferedReader(bufferSize = 4096),
                    RemoteModel::class.java)

            ioThread {
                data?.let { it ->
                    var insert = 0L
                    it.list.filter { model ->
                        model.word.isNotEmpty()
                    }
                            .map { model ->
                                var table: WordTable? = wordTableDao.getWord(model.word)

                                table = table?.copy(des = model.des) ?: model.toWordTable()

                                insert = wordTableDao.add(table)
                            }

                    result = if (insert > 0) {
                        MainActivity.isShown = false
                        Timber.i("Inserted: $insert")
                        spUtils.downloadDate = Date().time
                        Result.SUCCESS
                    } else {
                        Result.RETRY
                    }
                }

            }
        }

        return result
    }

}