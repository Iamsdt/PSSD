/*
 * Developed By Shudipto Trafder
 * on 8/17/18 1:06 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.iamsdt.androidextension.gone
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.ui.callback.ClickListener

class MainAdapter(
        private val clickListener: ClickListener) : ListAdapter<WordTable, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val model: WordTable? = getItem(position)

        if (model == null) {
            holder.itemView.gone()
        } else {
            holder.bind(model)

            //send model id to main activity
            holder.itemView.setOnClickListener {
                clickListener.onItemClick(model.id)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WordTable>() {
            override fun areItemsTheSame(oldConcert: WordTable,
                                         newConcert: WordTable): Boolean {
                return oldConcert.word == newConcert.word && oldConcert.bookmark == newConcert.bookmark
            }

            override fun areContentsTheSame(oldConcert: WordTable,
                                            newConcert: WordTable): Boolean {
                return oldConcert == newConcert
            }
        }
    }
}