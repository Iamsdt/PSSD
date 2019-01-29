package com.iamsdt.pssd.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


class DbHelper {
    companion object {
        val migration_1_2: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }

        //3 to 4
        val migration_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {


                //create new table
                database.execSQL("CREATE TABLE `USER` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `word` TEXT NOT NULL, `des` TEXT NOT NULL, `ref` TEXT NOT NULL, `recent` INTEGER NOT NULL, `bookmark` INTEGER NOT NULL, `addByUser` INTEGER NOT NULL, `uploaded` INTEGER NOT NULL)")


                //create new table
                database.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " word TEXT," +
                        " des TEXT," +
                        " ref TEXT," +
                        " recent INTEGER," +
                        " bookmark INTEGER," +
                        " addByUser INTEGER," +
                        " uploaded INTEGER)")

                //now copy table
                database.execSQL("Insert Into USER (id, word, des, bookmark, addByUser, uploaded) Select * From WordTable")

                //now drop table
                database.execSQL("Drop Table WordTable")

                //now rename table to new table
                database.execSQL("Alter Table USER RENAME TO WordTable")
            }
        }
    }
}