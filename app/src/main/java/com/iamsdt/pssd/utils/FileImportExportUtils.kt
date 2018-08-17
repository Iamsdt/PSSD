/*
 * Developed By Shudipto Trafder
 * on 8/17/18 11:46 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.iamsdt.pssd.database.WordTableDao
import java.io.File
import java.util.*

class FileImportExportUtils (val wordTableDao: WordTableDao) {

    /**
     * Export user favourite data
     * export in a text file just favourite word only
     * @param context for access sp
     */
    fun exportFileFavourite(context: Context?) {

    }

    /**
     * Export user added data
     * export in a text file just favourite word only
     * @param context for access sp
     */
    fun exportFileUser(context: Context?) {

    }

    //import file and added to database
    fun importFile(context: Context?, path: String?) {

        if (path == null) {
            Toast.makeText(context, "File not selected correctly", Toast.LENGTH_SHORT).show()
            return
        } else if (!path.contains(".txt")) {
            Toast.makeText(context, "Please input text file only", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(path)

        //contain inserted data uri
        val inserted = ArrayList<Uri>()
        //contain updated number
        val updated = ArrayList<Int>()


    }

    /**
     * Checking any file is available or not
     * if available then import it
     * if for first time app open
     * if user uninstall the app and reinstall again the it will work  */

    // TODO: 11/29/2017 add with backup agent or auto backup latter
    fun checkFileAvailable(context: Context?) {

    }

}
