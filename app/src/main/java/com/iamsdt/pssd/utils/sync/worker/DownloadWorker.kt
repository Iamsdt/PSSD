/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 4:57 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.sync.worker

import android.os.AsyncTask
import androidx.work.Worker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.model.OutputModel
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.File

class DownloadWorker : Worker(), KoinComponent {

    val gson: Gson by inject()

    val wordTableDao: WordTableDao by inject()

    val spUtils: SpUtils by inject()

    override fun doWork(): Result {

        var result = Result.SUCCESS

        val auth = FirebaseAuth.getInstance()
        auth.signInAnonymously().addOnCompleteListener {
            if (it.isSuccessful) {
                //write in  the database

                val db = FirebaseStorage.getInstance()
                val ref = db.reference.child(Constants.REMOTE.ADMIN)

                val file = File.createTempFile("download", ".json")

                ref.getFile(file).addOnSuccessListener {

                    val data = gson.fromJson(file.bufferedReader(bufferSize = 4096),
                            OutputModel::class.java)

                    data?.let {
                        AsyncTask.execute {
                            var insert = 0L
                            it.list.map { insert = wordTableDao.add(it) }

                            if (insert > 0) {
                                spUtils.saveDownloadDate()
                            } else {
                                result = Result.RETRY
                            }
                        }
                    }
                }

            } else result = Result.RETRY
        }

        return result
    }

}