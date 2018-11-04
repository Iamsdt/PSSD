/*
 * Developed By Shudipto Trafder
 *  on 8/30/18 4:54 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import android.widget.TextView

class TxtHelper(private val settingsUtils: SettingsUtils) {

    fun setSize(wordTV: TextView, desTV: TextView) {
        val (w, d) = settingsUtils.textSize
        wordTV.textSize = w + 4
        desTV.textSize = d
    }
}