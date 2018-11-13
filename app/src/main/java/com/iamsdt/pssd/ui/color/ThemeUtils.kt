/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 8:16 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.color

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.iamsdt.pssd.R
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.Constants.COLOR.NIGHT_MODE_SP_KEY
import com.iamsdt.pssd.utils.Constants.COLOR.NIGHT_MODE_VALUE_KEY

/**
 * Created by Shudipto Trafder on 1/18/2018.
 * at 9:05 PM
 */
class ThemeUtils {

    companion object {

        /**
         * This methods for select theme from
         * shared preference that saved in color activity
         *
         * @param activity to select theme
         */
        fun initialize(activity: Activity) {
            val sp: SharedPreferences = activity.getSharedPreferences(
                    Constants.COLOR.colorSp, Context.MODE_PRIVATE)

            val id = sp.getInt(Constants.COLOR.themeKey, R.style.AppTheme_NoActionBar)

            activity.setTheme(id)
            setNightMode(activity)
        }

        private fun setNightMode(context: Context) {

            if (getNightMode(context)) {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        fun turnOnOFNightMode(context: Context, value: Boolean) {
            val sp = context.getSharedPreferences(NIGHT_MODE_SP_KEY, Context.MODE_PRIVATE)
            sp.edit {
                putBoolean(NIGHT_MODE_VALUE_KEY, value)
            }
        }

        fun getNightMode(context: Context): Boolean {
            val sp = context.getSharedPreferences(NIGHT_MODE_SP_KEY, Context.MODE_PRIVATE)
            return sp.getBoolean(NIGHT_MODE_VALUE_KEY, false)
        }
    }
}