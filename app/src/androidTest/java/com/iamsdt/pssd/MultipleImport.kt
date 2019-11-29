/*
 * Developed By Shudipto Trafder
 *  on 8/27/18 10:19 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.iamsdt.pssd.database.MyDatabase
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultipleImport {

    private lateinit var wordTableDao: WordTableDao
    private lateinit var appContext: Context

    @Before
    fun init() {
        appContext = InstrumentationRegistry.getInstrumentation().context
        val db = Room.inMemoryDatabaseBuilder(
                appContext, MyDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        wordTableDao = db.wordTableDao

    }

    @Test
    suspend fun test() {
        val add = add()
        println("Data add: $add")

        val word = "Word 1"
        val des = "This is update des"

        //first search
        var data: WordTable? = wordTableDao.getWord(word)
        if (data == null) {
            data = WordTable(word = word, des = des)
        } else {
            data.des = des
        }

        wordTableDao.add(data)

        //check data inserted or not
        val insert = wordTableDao.getWord(word)
        println("Insert:$insert")
    }


    //add data to database
    private suspend fun add(): Long {
        val list = ArrayList<WordTable>()
        list.add(WordTable(word = "Word 1", des = "des 1"))
        list.add(WordTable(word = "Word 2", des = "des 2"))
        list.add(WordTable(word = "Word 3", des = "des 3"))
        list.add(WordTable(word = "Word 4", des = "des 4"))
        list.add(WordTable(word = "Word 5", des = "des 5"))
        list.add(WordTable(word = "Word 6", des = "des 6"))
        list.add(WordTable(word = "Word 7", des = "des 7"))
        list.add(WordTable(word = "Word 8", des = "des 8"))
        list.add(WordTable(word = "Word 9", des = "des 9"))

        var long = 0L
        list.map {
            long = wordTableDao.add(table = it)
        }
        return long
    }

}