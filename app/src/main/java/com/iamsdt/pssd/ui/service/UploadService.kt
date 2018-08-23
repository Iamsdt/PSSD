package com.iamsdt.pssd.ui.service

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.SpUtils
import com.iamsdt.pssd.utils.model.OutputModel
import dagger.android.AndroidInjection
import java.io.File
import java.io.FileWriter
import java.util.*
import javax.inject.Inject

class UploadService : Service() {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var wordTableDao: WordTableDao

    @Inject
    lateinit var spUtils: SpUtils

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null


    //in future it will be replaced by worker
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val thread = HandlerThread("data insert")
        thread.start()
        Handler(thread.looper).post {

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

                            if (up > 0){
                                spUtils.saveDate()
                            }
                        }
                    }
                }
            }

        }

        thread.quitSafely()

        return super.onStartCommand(intent, flags, startId)
    }
}