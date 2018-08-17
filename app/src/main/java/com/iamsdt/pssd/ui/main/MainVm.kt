/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:55 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import androidx.lifecycle.ViewModel
import com.iamsdt.pssd.database.WordTableDao

class MainVM constructor(val wordTableDao: WordTableDao)
    :ViewModel(){

    val getData = wordTableDao.getAllData()

}