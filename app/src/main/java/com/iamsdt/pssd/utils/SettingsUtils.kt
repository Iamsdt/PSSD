/*
 * Developed By Shudipto Trafder
 * on 8/19/18 9:46 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.iamsdt.pssd.R

class SettingsUtils(val context: Context) {

    //general

    val textSize
        get(): Pair<Float, Float> {
            val txt = settingsSp.getString(
                    context.getString(R.string.textSizeKey),
                    context.getString(R.string.sTextModerateValue)) ?: "20"

            val size = txt.toFloat()

            return Pair(size, size - 2)
        }

    val interval
        get(): Int {
            val days: String = settingsSp.getString(
                    context.getString(R.string.syncKey),
                    context.getString(R.string.syncWeekTwoValue)
            ) ?: "15" //default 7

            return days.toInt()
        }

    val shareStatus
        get():Boolean = settingsSp.getBoolean(
                context.getString(R.string.switchShare), true)

    val searchIcon
        get():Boolean =
            settingsSp.getBoolean(context.getString(R.string.switchSearch),
                    false)


    //advance

    var filePath: String
        get() = settingsSp.getString(context.getString(R.string.advance_dir_add_key), Constants.Settings.DEFAULT_PATH_STORAGE)
                ?: ""
        set(value) = settingsSp.edit {
            putString(context.getString(R.string.advance_dir_add_key), value)
        }

    private val settingsSp
        get():SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

}