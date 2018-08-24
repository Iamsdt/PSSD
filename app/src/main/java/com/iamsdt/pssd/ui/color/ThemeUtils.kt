
/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 8:16 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.color
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.iamsdt.pssd.R
import com.iamsdt.pssd.utils.Constants

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
        }
    }
}