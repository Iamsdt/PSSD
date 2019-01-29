/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:39 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class WordTable(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var word: String = "",
        var des: String = "",
        var ref: String = "Added by user",
        var recent: Date = Date(),
        var bookmark: Boolean = false,
        var addByUser: Boolean = false,
        var uploaded: Boolean = false
)