/*
 * Developed By Shudipto Trafder
 *  on 10/26/18 4:42 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils

import androidx.paging.PositionalDataSource
import com.iamsdt.pssd.database.WordTable

/**
 * Created by Shudipto Trafder on 10/26/2018.
 * at 4:42 PM
 */
class MyDataSource:PositionalDataSource<WordTable>(){
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<WordTable>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<WordTable>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}