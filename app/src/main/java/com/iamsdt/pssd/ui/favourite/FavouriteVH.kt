/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 9:44 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.favourite

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.androidextension.addText
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTable
import kotlinx.android.synthetic.main.bookmark_list.view.*
import kotlinx.android.synthetic.main.fav_list.view.*
import kotlinx.android.synthetic.main.undo_layout.view.*

class FavouriteVH(val view: View)
    : RecyclerView.ViewHolder(view) {

    private val wordTV: AppCompatTextView = view.wordTV
    private val desTV: AppCompatTextView = view.desTV
    val favIcon: ImageView = view.favIcon
    val regular: View = view.regular_layout
    val swipe: View = view.swipeLayout
    val delete: TextView = view.undo_deleteTxt
    val undo: TextView = view.undoBtn

    fun bind(wordTable: WordTable) {
        wordTV.addText(wordTable.word)
        desTV.addText(wordTable.des)

        // complete: 8/17/2018 favourite icon
        if (wordTable.bookmark) {
            favIcon.setImageDrawable(view.context.getDrawable(R.drawable.ic_like_fill))
        } else {
            favIcon.setImageDrawable(view.context.getDrawable(R.drawable.ic_like_blank))
        }
    }

}