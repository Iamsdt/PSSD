/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 6:38 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.flash

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedList
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.ext.addStr
import kotlinx.android.synthetic.main.flash_item.view.*
import timber.log.Timber

class FlashAdapter(val click:ClickListener) :
        RecyclerView.Adapter<FlashAdapter.MyVH>() {

    private var dataList:PagedList<WordTable> ?= null

    override fun getItemCount(): Int = dataList?.size ?: 0

    interface ClickListener{
        fun click(id:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.flash_item, parent, false)

        return MyVH(view)
    }

    fun submitList(list: PagedList<WordTable>){
        dataList = list
        AsyncListDiffer<WordTable>(this, DIFF_CALLBACK).submitList(list)
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {

        val model: WordTable? = dataList?.get(position)

        model?.let {
            holder.bind(it)

            holder.itemView.setOnClickListener {
                click.click(model.id)
                Timber.i("Tag set:${model.id}")
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

                return oldConcert.id == newConcert.id && oldConcert.bookmark == newConcert.bookmark
            }

            override fun areContentsTheSame(oldConcert: WordTable,
                                            newConcert: WordTable): Boolean {
                return oldConcert == newConcert
            }
        }
    }

    inner class MyVH(view:View):RecyclerView.ViewHolder(view){

        val word:TextView = view.word

        fun bind(wordTable: WordTable){
            word.addStr(wordTable.word)
        }
    }
}