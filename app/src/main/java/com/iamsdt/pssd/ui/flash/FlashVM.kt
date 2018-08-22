/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 6:38 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.flash

import androidx.lifecycle.ViewModel
import com.iamsdt.pssd.database.WordTableDao
import javax.inject.Inject

class FlashVM @Inject constructor(val wordTableDao: WordTableDao):ViewModel(){

    val data get() = wordTableDao.getBookmarkData()

}