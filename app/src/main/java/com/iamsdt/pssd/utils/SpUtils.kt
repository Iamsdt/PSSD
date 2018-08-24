/*
 * Developed By Shudipto Trafder
 * on 8/17/18 2:59 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.iamsdt.pssd.utils.Constants.REMOTE.DATE_DOWNLOAD
import com.iamsdt.pssd.utils.Constants.REMOTE.DATE_UPLOAD
import com.iamsdt.pssd.utils.Constants.SP.DATA_INSERT
import com.iamsdt.pssd.utils.Constants.SP.DATA_VOLUME
import com.iamsdt.pssd.utils.Constants.SP.FIRST_TIME
import java.util.*

class SpUtils(val context: Context) {

    val isAppRunFirstTime get() = appSp.getBoolean(FIRST_TIME, true)

    val isDatabaseInserted get() = appSp.getBoolean(DATA_INSERT, false)

    fun setDataInserted(){
        appSp.edit {
            putBoolean(DATA_INSERT,true)
        }
    }

    fun setAppRunFirstTime() {
        appSp.edit {
            putBoolean(FIRST_TIME, false)
        }
    }


    val getVolume get() = appSp.getInt(DATA_VOLUME, 1)

    fun setDataVolume(int: Int) {
        appSp.edit {
            putInt(DATA_VOLUME, int)
        }
    }

    //app sp
    private val appSp
        get():SharedPreferences =
            context.getSharedPreferences(Constants.SP.appSp,
                    Context.MODE_PRIVATE)



    //get date
    val dateUpload = remoteSp.getLong(DATE_UPLOAD,0)
    val dateDownload = remoteSp.getLong(DATE_DOWNLOAD,0)

    //save date
    fun saveUploadDate(date:Long = Date().time){
        remoteSp.edit {
            putLong(DATE_UPLOAD,date)
        }
    }

    fun saveDownloadDate(date:Long = Date().time){
        remoteSp.edit {
            putLong(DATE_DOWNLOAD,date)
        }
    }

    private val remoteSp
        get():SharedPreferences =
            context.getSharedPreferences(Constants.REMOTE.SP,
                    Context.MODE_PRIVATE)
}