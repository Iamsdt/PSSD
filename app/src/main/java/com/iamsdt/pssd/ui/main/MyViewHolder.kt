/*
 * Developed By Shudipto Trafder
 * on 8/17/18 1:15 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.ext.addStr
import com.iamsdt.pssd.ext.gone
import kotlinx.android.synthetic.main.main_item.view.*

class MyViewHolder(val view:View)
    :RecyclerView.ViewHolder(view){

    private val wordTV:TextView = view.wordTV
    private val desTV:TextView = view.desTV
    val favIcon: ImageView = view.favIcon

    fun bind(wordTable: WordTable){
        wordTV.addStr(wordTable.word)
        desTV.addStr(wordTable.des)

        // complete: 8/17/2018 favourite icon
//        if (wordTable.bookmark){
//            favIcon.setImageDrawable(view.context.getDrawable(R.drawable.ic_like_fill))
//        } else{
//            favIcon.setImageDrawable(view.context.getDrawable(R.drawable.ic_like_blank))
//        }

        //hide fav icon
        favIcon.gone()
    }

}