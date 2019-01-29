/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:55 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ext

import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.utils.model.Model

fun CharSequence.toDouble(): Double {
    val sequence = this.toString()
    return sequence.toDoubleOrNull() ?: 0.0
}

//convert model to word table
fun Model.toWordTable() = WordTable(word = word, des = des, reference = ref)

fun CharSequence.toCapFirst(): String {
    val c = this[0].toUpperCase()
    val s = this.toString()
    return s.replaceFirst(this[0], c)
}