package com.iamsdt.pssd.utils.sync.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.model.Model
import com.iamsdt.pssd.utils.model.RemoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.File
import java.io.FileWriter
import java.util.*
import kotlin.collections.ArrayList

class UploadWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters), KoinComponent {

    private val gson: Gson by inject()

    private val wordTableDao: WordTableDao by inject()

    private val spUtils: SpUtils by inject()

    private val bgScope = CoroutineScope(Dispatchers.IO)

    //todo change to firebase firestone

    override fun doWork(): Result {

        var result = Result.success()

        //login
        val auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if (user == null) {
            auth.signInAnonymously().addOnCompleteListener { task ->
                result = if (task.isSuccessful) {
                    //write in  the database
                    writeDB(task.result?.user)
                } else Result.retry()
            }
        } else {
            result = writeDB(user)
        }

        return result
    }

    private fun writeDB(user: FirebaseUser?): Result {

        var result = Result.success()

        bgScope.launch {
            val data = wordTableDao.upload()
            val uid = user?.uid ?: ""
            val db = FirebaseFirestore.getInstance()
            var up = 0
            if (data.isNotEmpty()) {
                data.forEach {
                    val path = it.word + Date().time
                    db.collection("User").document(path)
                            .set(it)
                            .addOnCompleteListener { t ->
                                if (t.isSuccessful) {
                                    launch {
                                        data.forEach { word ->
                                            up = wordTableDao.update((word.copy(uploaded = true)))
                                        }
                                    }
                                } else {
                                    result = Result.retry()
                                }
                            }
                }
            }
            if (up > 0) {
                spUtils.uploadDate = Date().time
            } else {
                result = Result.retry()
            }


        }
        return result
    }

    override fun onStopped() {
        super.onStopped()
        bgScope.coroutineContext.cancelChildren()
    }

    private fun getFile(data: List<WordTable>): File {

        val list: ArrayList<Model> = ArrayList()
        data.map {
            list.add(Model(it.word, it.des, ""))
        }

        val outputData = RemoteModel(
                "User", Date().time, list)

        //create json
        val string = gson.toJson(outputData)

        val file = File.createTempFile("upload", ".json")
        val writer = FileWriter(file)
        writer.write(string)
        writer.close()

        return file
    }


}