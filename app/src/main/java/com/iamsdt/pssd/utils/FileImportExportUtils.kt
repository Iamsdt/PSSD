/*
 * Developed By Shudipto Trafder
 * on 8/19/18 10:14 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.os.AsyncTask
import com.google.gson.Gson
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.SingleLiveEvent
import com.iamsdt.pssd.utils.Constants.IO.EXPORT_ADD
import com.iamsdt.pssd.utils.Constants.IO.EXPORT_FAV
import com.iamsdt.pssd.utils.Constants.Settings.EXT
import com.iamsdt.pssd.utils.Constants.Settings.SETTING_IMOUT_OPTION_FAVOURITE
import com.iamsdt.pssd.utils.Constants.Settings.SETTING_IMOUT_OPTION_USER
import com.iamsdt.pssd.utils.model.OutputModel
import com.iamsdt.pssd.utils.model.StatusModel
import java.io.File
import java.io.FileFilter
import java.io.FileWriter
import java.util.*

class FileImportExportUtils(val wordTableDao: WordTableDao,
                            val settingsUtils: SettingsUtils,
                            val gson: Gson) {

    /**
     * Export user favourite data
     * export in a text file just favourite word only
     */
    fun exportFileFavourite() {
        AsyncTask.execute {
            val list = wordTableDao.getBookmarkList()
            if (list.isNotEmpty()) {
                generateFile(list, "favourite")
            } else {
                ioStatus.postValue(StatusModel(false,
                        EXPORT_FAV, "No favourite words found. Please add some"))
            }
        }
    }

    /**
     * This method will generate file with data
     * @param data actual data in list
     * @param type type of data
     */

    private fun generateFile(data: List<WordTable>, type: String) {
        val outputData = OutputModel(
                type, Date().time, data)

        //convert output file to string
        val string = gson.toJson(outputData)

        //now file
        val dir = File(settingsUtils.getPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file = if (type == "favourite")
            File(dir, SETTING_IMOUT_OPTION_FAVOURITE)
        else File(dir, SETTING_IMOUT_OPTION_USER)

        if (file.exists())
            file.setWritable(true)
        else
            file.createNewFile()

        val writer = FileWriter(file)
        writer.write(string)
        writer.close()

        val model = if (type == "favourite") {
            StatusModel(true, EXPORT_FAV,
                    "Favourite words saved on ${file.absolutePath}")
        } else StatusModel(true, EXPORT_ADD, "Your Added words saved on ${file.absolutePath}")

        ioStatus.postValue(model)
    }

    /**
     * Export user added data
     * export in a text file just favourite word only
     */
    fun exportFileUser() {
        val list = wordTableDao.getAddedWordList()
        if (list.isNotEmpty()) {
            generateFile(list, "Added")
        } else {
            ioStatus.postValue(StatusModel(false,
                    EXPORT_FAV, "No Added words found. Please add some"))
        }
    }

    //import file and added to database
    fun importFile(path: String?, title: String) {

        if (path == null) {
            ioStatus.value = StatusModel(false, title,
                    "File not selected correctly")
            return
        } else if (!path.contains(EXT)) {
            ioStatus.value = StatusModel(false, title,
                    "Please select correct file")
            return
        }

        val file = File(path)
        val data = gson.fromJson(file.bufferedReader(bufferSize = 4096),
                OutputModel::class.java)

        data?.let {
            AsyncTask.execute {
                var insert = 0L
                it.list.map { insert = wordTableDao.add(it) }

                if (insert > 0) {
                    val model = if (title == EXPORT_FAV)
                        StatusModel(true, title, "")
                    else StatusModel(true, title, "")
                    //post value
                    ioStatus.postValue(model)

                } else {
                    val model = if (title == EXPORT_FAV)
                        StatusModel(true, title, "")
                    else StatusModel(true, title, "")
                    //post value
                    ioStatus.postValue(model)
                }
            }
        }
    }

    /**
     * Checking any file is available or not
     * if available then import it
     * if for first time app open
     * if user uninstall the app and reinstall again the it will work  */

    fun checkFileAvailable() {
        val dir = File(settingsUtils.getPath)

        if (!dir.exists()) return

        //get list of file
        //only .ss file will read
        AsyncTask.execute {
            dir.listFiles(FileFilter {
                it?.name?.contains(EXT) == true
            }).map {
                val data = gson.fromJson(it.bufferedReader(bufferSize = 4096),
                        OutputModel::class.java)

                data?.let { word ->
                    word.list.map { table -> wordTableDao.add(table) }
                }
            }
        }
    }

    companion object {
        val ioStatus = SingleLiveEvent<StatusModel>()
    }

}
