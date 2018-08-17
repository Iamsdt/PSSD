/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:45 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.iamsdt.pssd.database.MyDatabase
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.blockingObserve
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest{

    lateinit var wordTableDao: WordTableDao

    @Before
    fun init(){
        val appContext = InstrumentationRegistry.getTargetContext()
        val db = Room.inMemoryDatabaseBuilder(
                appContext,MyDatabase::class.java).build()

        wordTableDao = db.wordTableDao
    }

    @Test
    fun test(){
        val add = add()
        println("Data add: $add")

        //now access data
        val data = wordTableDao.getAllData()
        data.blockingObserve()?.forEach {
            println("Word: ${it.word}")
        }

    }


    //add data to database
    fun add():Long{
        val list = ArrayList<WordTable>()
        list.add(WordTable(word = "Word 1",des = "des 1"))
        list.add(WordTable(word = "Word 2",des = "des 2"))
        list.add(WordTable(word = "Word 3",des = "des 3"))
        list.add(WordTable(word = "Word 4",des = "des 4"))
        list.add(WordTable(word = "Word 5",des = "des 5"))
        list.add(WordTable(word = "Word 6",des = "des 6"))
        list.add(WordTable(word = "Word 7",des = "des 7"))
        list.add(WordTable(word = "Word 8",des = "des 8"))
        list.add(WordTable(word = "Word 9",des = "des 9"))

        var long = 0L
        list.map {
            long = wordTableDao.add(it)
        }
        return long
    }

}