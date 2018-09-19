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

    var isAppRunFirstTime: Boolean
        get() = appSp.getBoolean(FIRST_TIME, true)
        set(v) = appSp.edit {
            putBoolean(FIRST_TIME, v)
        }

    var isDatabaseInserted: Boolean
        get() = appSp.getBoolean(DATA_INSERT, false)
        set(value) = appSp.edit {
            putBoolean(DATA_INSERT, value)
        }

    var dataVolume: Int
        get() = appSp.getInt(DATA_VOLUME, 1)
        set(value) = appSp.edit {
            putInt(DATA_VOLUME, value)
        }

    //app sp
    private val appSp
        get():SharedPreferences =
            context.getSharedPreferences(Constants.SP.appSp,
                    Context.MODE_PRIVATE)


    //get date
    var uploadDate: Long
        get() = remoteSp.getLong(DATE_UPLOAD, 0)
        set(value) = remoteSp.edit {
            putLong(DATE_UPLOAD, value)
        }
    var downloadDate: Long
        get() = remoteSp.getLong(DATE_DOWNLOAD, 0)
        set(value) = remoteSp.edit {
            putLong(DATE_DOWNLOAD, value)
        }

    //remote sp
    private val remoteSp
        get():SharedPreferences =
            context.getSharedPreferences(Constants.REMOTE.SP,
                    Context.MODE_PRIVATE)
}