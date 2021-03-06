package com.iamsdt.pssd.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.crashlytics.android.Crashlytics
import timber.log.Timber


class DbHelper {
    companion object {

        val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }

        val migration_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }

        val migration_1_4: Migration = object : Migration(1, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `reference` TEXT DEFAULT '' NOT NULL")
                    database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `recent` INTEGER DEFAULT 0 NOT NULL")
                } catch (e: Exception) {
                    Timber.e(e, "Error in database alter")
                    Crashlytics.logException(e)
                }
            }
        }

        val migration_2_4: Migration = object : Migration(2, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `reference` TEXT DEFAULT '' NOT NULL")
                    database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `recent` INTEGER DEFAULT 0 NOT NULL")
                } catch (e: Exception) {
                    Timber.e(e, "Error in database alter")
                    Crashlytics.logException(e)
                }
            }
        }

        //3 to 4
        val migration_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                try {
                    database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `reference` TEXT DEFAULT '' NOT NULL")
                    database.execSQL("ALTER TABLE `WordTable` ADD COLUMN `recent` INTEGER DEFAULT 0 NOT NULL")
                } catch (e: Exception) {
                    Timber.e(e, "Error in database alter")
                    Crashlytics.logException(e)
                }
            }
        }

        val migration_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //nothing
            }
        }
    }
}