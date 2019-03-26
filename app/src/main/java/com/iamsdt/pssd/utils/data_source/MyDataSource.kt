package com.iamsdt.pssd.utils.data_source

import androidx.paging.PositionalDataSource
import com.iamsdt.pssd.database.WordTable

class MyDataSource:PositionalDataSource<WordTable>(){

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<WordTable>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<WordTable>) {

    }

}