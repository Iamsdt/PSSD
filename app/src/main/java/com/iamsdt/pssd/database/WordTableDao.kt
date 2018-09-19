/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:40 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface WordTableDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(table: WordTable):Long

    @Update
    fun update(table: WordTable):Int

    @Delete
    fun delete(pageTable: WordTable):Int

    @Query("Select * From WordTable")
    fun getAllData():DataSource.Factory<Int,WordTable>

    @Query("Select * From WordTable")
    fun getAllList():List<WordTable>

    @Query("Update WordTable set bookmark = 1 where id = :id ")
    fun setBookmark(id:Int):Int

    @Query("Update WordTable set bookmark = 0 where id = :id ")
    fun deleteBookmark(id:Int):Int

    @Query("Select * From WordTable where bookmark = 1")
    fun getBookmarkData():DataSource.Factory<Int,WordTable>

    @Query("Select * From WordTable where bookmark = 1")
    fun getBookmarkList():List<WordTable>

    @Query("Select * From WordTable where id =:id")
    fun getSingleWord(id: Int):LiveData<WordTable>

    @Query("Select * From WordTable where word =:word")
    fun getWord(word: String):WordTable

    @Query("Select * From WordTable where addByUser = 1")
    fun getAddedWordList():List<WordTable>

    @Query("Select * From WordTable where addByUser = 1")
    fun getAddedWordByUser():DataSource.Factory<Int,WordTable>

    @Query("Select * From WordTable where word like '%' ||:query ")
    fun getSearchData(query: String):DataSource.Factory<Int,WordTable>

    @Query("Select * From WordTable where word = :word")
    fun getSearchResult(word:String):WordTable

    @Query("Select * From WordTable order by Random() limit 1")
    fun getRandomData():LiveData<WordTable>

    @Query("Select * From WordTable where uploaded = 0")
    fun upload():List<WordTable>
}