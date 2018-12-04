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

/**
 * Created by Shudipto Trafder on 12/1/2018.
 * at 6:46 PM
 */

@RunWith(AndroidJUnit4::class)
class UploadCheck {

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
    fun test() {
        val add = add()
        println("Data add: $add")

        val list = wordTableDao.upload()
        println(list.size)
        list.forEach {
            println("${it.word} user:${it.addByUser} upload:${it.uploaded}")
        }
    }


    //add data to database
    private fun add(): Long {
        val list = ArrayList<WordTable>()
        list.add(WordTable(word = "Word 1", des = "des 1", addByUser = true, uploaded = false))
        list.add(WordTable(word = "Word 2", des = "des 1", addByUser = true, uploaded = true))
        list.add(WordTable(word = "Word 3", des = "des 1", addByUser = true, uploaded = false))
        list.add(WordTable(word = "Word 4", des = "des 1", addByUser = true, uploaded = true))
        list.add(WordTable(word = "Word 5", des = "des 1", addByUser = true, uploaded = false))
        list.add(WordTable(word = "Word 6", des = "des 1", addByUser = true, uploaded = false))
        list.add(WordTable(word = "Word 7", des = "des 1", addByUser = true, uploaded = true))
        list.add(WordTable(word = "Word 8", des = "des 1", addByUser = true, uploaded = false))
        list.add(WordTable(word = "Word 9", des = "des 1", addByUser = true, uploaded = true))
        list.add(WordTable(word = "Word 10", des = "des 1", addByUser = true, uploaded = false))
        list.add(WordTable(word = "Word 11", des = "des 1", addByUser = true, uploaded = true))
        list.add(WordTable(word = "Word 12", des = "des 1", addByUser = true, uploaded = true))
        list.add(WordTable(word = "Word 13", des = "des 1", addByUser = true, uploaded = false))

        println("List Size: ${list.size}")

        var long = 0L
        list.map {
            long = wordTableDao.add(it)
        }
        return long
    }
}