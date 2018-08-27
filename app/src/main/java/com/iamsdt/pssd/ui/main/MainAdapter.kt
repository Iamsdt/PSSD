/*
 * Developed By Shudipto Trafder
 * on 8/17/18 1:06 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.ui.details.DetailsActivity

class MainAdapter(val context:Context) :PagedListAdapter<WordTable, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val model:WordTable ?= getItem(position)

        model?.let {
            holder.bind(it)

            holder.itemView.setOnClickListener {
                val intent = Intent(context,DetailsActivity::class.java)
                intent.putExtra(Intent.EXTRA_TEXT,model.word)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WordTable>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(oldConcert: WordTable,
                                         newConcert: WordTable): Boolean {

//                Timber.i("compare callback item ${oldConcert.id}:${newConcert.id} " +
//                        "${oldConcert.bookmark}:${newConcert.bookmark}")

                return oldConcert.word == newConcert.word && oldConcert.bookmark == newConcert.bookmark
            }

            override fun areContentsTheSame(oldConcert: WordTable,
                                            newConcert: WordTable): Boolean {
                return oldConcert == newConcert
            }
        }
    }
}