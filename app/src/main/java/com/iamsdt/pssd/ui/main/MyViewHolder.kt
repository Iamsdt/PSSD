/*
 * Developed By Shudipto Trafder
 * on 8/17/18 1:15 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.ext.addStr
import kotlinx.android.synthetic.main.main_item.view.*

class MyViewHolder(val view: View)
    : RecyclerView.ViewHolder(view) {

    private val wordTV: TextView = view.wordTV
    private val desTV: TextView = view.desTV

    fun bind(wordTable: WordTable) {
        wordTV.addStr(wordTable.word)
        desTV.addStr(wordTable.des)
    }

}