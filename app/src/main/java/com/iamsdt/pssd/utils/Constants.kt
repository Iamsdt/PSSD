/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:39 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.os.Environment

class Constants {

    companion object {
        const val DB_NAME = "SS.db"

        const val SEARCH = "Search"
        const val SEARCH_DATA = "Search_Data"

        const val WORD_ID = "ID"


    }

    object ADD{
        const val WORD = "word"
        const val DES = "des"
        const val DIALOG = "Dialog"
    }

    object COLOR{
        const val colorSp = "colorSp"
        const val themeKey = "themeKey"
        const val NIGHT_MODE_SP_KEY: String = "NightModeSp"
        const val NIGHT_MODE_VALUE_KEY: String = "NightSP"
    }

    object REMOTE {
        const val FB_REMOTE_CONFIG_STORAGE_KEY = "storage_key"
        const val USER = "user"
        const val ADMIN = "admin"

        const val SP = "remote_sp"
        const val DATE_UPLOAD = "date_upload"
        const val DATE_DOWNLOAD = "date_download"
    }

    object SP {
        const val appSp = "App"
        const val FIRST_TIME = "first"
        const val DATA_INSERT = "data"

        const val DATA_VOLUME = "volume"
    }

    object Settings {
        const val EXT = ".ss"

        val SETTING_IMOUT_OPTION_FAVOURITE = "favourite$EXT"
        val SETTING_IMOUT_OPTION_USER = "user$EXT"


        val DEFAULT_PATH_STORAGE = Environment.getExternalStorageDirectory()
                .absolutePath + "/SSDictionary/"

        const val STORAGE_PATH_KEY = "storage"
        const val STORAGE_PATH = "path"

    }

    object IO {
        const val EXPORT_FAV = "Export_Fav"
        const val EXPORT_ADD = "Export_Add"

        const val IMPORT_ADD = "IMPORT_Add"
        const val IMPORT_FAV = "IMPORT_Fav"

        const val BACKUP = "backup"
    }

}