/*
 * Developed By Shudipto Trafder
 *  on 9/19/18 9:27 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import com.google.gson.Gson
import com.iamsdt.androidextension.SingleLiveEvent
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.Constants.SP.DATA_RESTORE
import com.iamsdt.pssd.utils.model.OutputModel
import com.iamsdt.pssd.utils.model.StatusModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileFilter


class RestoreData(private val settingsUtils: SettingsUtils,
                  private val gson: Gson,
                  private val wordTableDao: WordTableDao,
                  private val spUtils: SpUtils) {
    /**
     * Checking any file is available or not
     * if available then import it
     * if for first time app open
     * if user uninstall the app and reinstall again the it will work
     */
    fun isFound(): Boolean {

        if (spUtils.restore) {
            return false
        }

        val dir = File(settingsUtils.filePath)

        if (!dir.exists()) return false

        //get list of file
        //only .ss file will read

        val s = dir.listFiles(FileFilter {
            it?.name?.contains(Constants.Settings.EXT) == true
        })

        return s?.isNotEmpty() ?: false
    }

    fun restoreFile() {
        val dir = File(settingsUtils.filePath)

        if (!dir.exists()) return

        //get list of file
        //only .ss file will read

        GlobalScope.launch(Dispatchers.IO) {

            var size = 0L

            dir.listFiles(FileFilter {
                it?.name?.contains(Constants.Settings.EXT) == true
            })?.forEach { file ->
                val data = gson.fromJson(file.bufferedReader(bufferSize = 4096),
                        OutputModel::class.java)

                //added to the database
                data?.let { it.list.map { table -> size = wordTableDao.add(table) } }
            }

            if (size >= 0L) {
                ioStatus.postValue(StatusModel(true, DATA_RESTORE, "Data Restore complete"))
                spUtils.restore = true
            }
        }
    }

    companion object {
        val ioStatus = SingleLiveEvent<StatusModel>()
    }
}
