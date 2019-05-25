/*
 * Developed By Shudipto Trafder
 * on 8/17/18 1:15 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.androidextension.addText
import com.iamsdt.pssd.database.WordTable
import kotlinx.android.synthetic.main.main_item.view.*

class MyViewHolder(val view: View)
    : RecyclerView.ViewHolder(view) {

    private val wordTV: AppCompatTextView = view.wordTV
    private val desTV: AppCompatTextView = view.desTV

    fun bind(wordTable: WordTable) {
        wordTV.addText(wordTable.word)
        desTV.addText(wordTable.des)
    }

}