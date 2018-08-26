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
import com.iamsdt.pssd.ui.settings.BackupSettings
import com.iamsdt.pssd.utils.Constants.IO.EXPORT_ADD
import com.iamsdt.pssd.utils.Constants.IO.EXPORT_FAV
import com.iamsdt.pssd.utils.Constants.Settings.EXT
import com.iamsdt.pssd.utils.Constants.Settings.SETTING_IMOUT_OPTION_FAVOURITE
import com.iamsdt.pssd.utils.Constants.Settings.SETTING_IMOUT_OPTION_USER
import com.iamsdt.pssd.utils.model.OutputModel
import com.iamsdt.pssd.utils.model.StatusModel
import timber.log.Timber
import java.io.File
import java.io.FileFilter
import java.io.FileWriter
import java.io.IOException
import java.util.*

class FileImportExportUtils(private val wordTableDao: WordTableDao,
                            private val settingsUtils: SettingsUtils,
                            private val gson: Gson) {

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
                BackupSettings.isShown = false
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

        var file = if (type == "favourite")
            File(dir, SETTING_IMOUT_OPTION_FAVOURITE)
        else File(dir, SETTING_IMOUT_OPTION_USER)

        try {
            if (file.exists())
                file.setWritable(true)
            else {
                file.createNewFile()
                file.setWritable(true)
            }
        } catch (e: IOException) {
            Timber.i(e, "file error: ${file.absolutePath}")
            // Complete: 8/22/18 add default location
            //show message.
            BackupSettings.isShown = false
            ioStatus.postValue(StatusModel(false,
                    EXPORT_FAV, "Can not able to save file to this ${file.absolutePath} location." +
                    " Saving file in the default location"))
            val name = file.name
            file = File(Constants.Settings.DEFAULT_PATH_STORAGE, name)
        } finally {
            if (file.exists()) {
                file.setWritable(true)

            } else {
                file.createNewFile()
                file.setWritable(true)

            }

            val writer = FileWriter(file)
            writer.write(string)
            writer.close()

            Thread {
                try {
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    BackupSettings.isShown = false
                    val model = if (type == "favourite") {
                        StatusModel(true, EXPORT_FAV,
                                "Favourite words saved on ${file.absolutePath}")
                    } else StatusModel(true, EXPORT_ADD, "Your Added words saved on ${file.absolutePath}")

                    ioStatus.postValue(model)
                }
            }.start()

        }
    }

    /**
     * Export user added data
     * export in a text file just favourite word only
     */
    fun exportFileUser() {
        AsyncTask.execute {
            val list = wordTableDao.getAddedWordList()
            if (list.isNotEmpty()) {
                generateFile(list, "Added")
            } else {
                BackupSettings.isShown = false
                ioStatus.postValue(StatusModel(false,
                        EXPORT_ADD, "No Added words found. Please add some"))
            }
        }
    }

    //import file and added to database
    fun importFile(path: String?, title: String) {
        BackupSettings.isShown = false
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
