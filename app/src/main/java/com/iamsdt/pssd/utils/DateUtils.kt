/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 4:48 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import org.joda.time.DateTime
import org.joda.time.Days
import timber.log.Timber
import java.util.*

class DateUtils {

    companion object {

        fun getDayInterval(oldDate: Long): Int {

            val today = DateTime(Date())
            val preDate = DateTime(oldDate)

            val day = Days.daysBetween(preDate, today).days

            Timber.i(day.toString())

            return day
        }
    }
}