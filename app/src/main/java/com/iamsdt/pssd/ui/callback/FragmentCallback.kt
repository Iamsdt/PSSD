/*
 * Developed By Shudipto Trafder
 *  on 9/22/18 11:39 AM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.callback

import androidx.paging.PagedList
import com.iamsdt.pssd.database.WordTable

/**
 * Created by Shudipto Trafder on 9/22/2018.
 * at 11:39 AM
 */

interface FragmentCallback{

    fun wordDataList(it:PagedList<WordTable>)

}