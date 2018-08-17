/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:39 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.os.Environment

class Constants{

    companion object {
        const val DB_NAME = "SS.db"
    }

    object SP{
        const val appSp = "App"
        const val FIRST_TIME= "first"
        const val DATA_INSERT = "data"

        const val DATA_VOLUME = "volume"
    }

    object Settings{
        const val SETTING_IMOUT_OPTION_FAVOURITE = "favourite.txt"
        const val SETTING_IMOUT_OPTION_USER = "user.txt"


        val DEFAULT_PATH_STORAGE = Environment.getExternalStorageDirectory()
                .absolutePath + "/SSDictionary/"

        const val STORAGE_PATH_KEY = "storage"
        const val STORAGE_PATH = "path"

    }

}