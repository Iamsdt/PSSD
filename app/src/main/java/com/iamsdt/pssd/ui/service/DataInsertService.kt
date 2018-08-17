/*
 * Developed By Shudipto Trafder
 * on 8/17/18 3:04 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.SpUtils
import dagger.android.AndroidInjection
import timber.log.Timber
import java.io.InputStreamReader
import javax.inject.Inject


class DataInsertService:Service(){

    @Inject
    lateinit var wordTableDao: WordTableDao

    @Inject
    lateinit var spUtils: SpUtils

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //command
        val stream = assets.open("data.json")

        val reader = InputStreamReader(stream)

        // TODO: 8/17/2018 replace with corotines

        val thread = HandlerThread("data insert")
        thread.start()
        Handler(thread.looper).post {
            val data = Gson().fromJson(reader.buffered(4096),JsonModel::class.java)

            var addItem = 0

            data?.let {
                spUtils.setDataVolume(it.volume)
                it.collection.forEach {
                    val wordTable = WordTable(word = it.word,des = it.des)
                    val count= wordTableDao.add(wordTable)
                    Timber.i("Add $count")
                    addItem++
                }
            }

            if (addItem > 0){
                spUtils.setDataInserted()
            }

            Timber.i("Total added: $addItem")
        }

        thread.quitSafely()

        return super.onStartCommand(intent, flags, startId)
    }

}