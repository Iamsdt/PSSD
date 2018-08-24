package com.iamsdt.pssd.utils.sync.worker

import android.net.Uri
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
import java.io.FileWriter
import java.util.*

class UploadWorker : Worker(), KoinComponent {

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
                val ref = db.reference.child(Constants.REMOTE.USER)
                        .child(it.result.user.uid)

                val data = wordTableDao.upload()

                val outputData = OutputModel(
                        "User", Date().time, data)

                val string = gson.toJson(outputData)

                val file = File.createTempFile("upload", ".json")
                val writer = FileWriter(file)
                writer.write(string)
                writer.close()

                ref.putFile(Uri.fromFile(file)).addOnCompleteListener {
                    if (it.isSuccessful) {
                        var up = 0
                        data.forEach {
                            val word = it
                            word.uploaded = true
                            up = wordTableDao.update((word))
                        }

                        if (up <= 0) {
                            spUtils.saveUploadDate()
                        } else{
                            result = Result.RETRY
                        }
                    } else{
                        result = Result.RETRY
                    }
                }
            } else result = Result.RETRY
        }

        return result
    }


}