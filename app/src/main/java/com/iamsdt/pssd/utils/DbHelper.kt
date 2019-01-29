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
                database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `reference` TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `recent` INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }
}