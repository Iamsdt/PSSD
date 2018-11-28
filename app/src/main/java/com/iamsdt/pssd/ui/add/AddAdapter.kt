/*
 * Developed By Shudipto Trafder
 *  on 8/27/18 11:03 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.add

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.gone
import com.iamsdt.pssd.ext.show
import com.iamsdt.pssd.ui.details.DetailsActivity
import com.iamsdt.pssd.ui.favourite.FavouriteVH
import com.iamsdt.pssd.ui.main.MainAdapter.Companion.DIFF_CALLBACK
import es.dmoral.toasty.Toasty

class AddAdapter(var context: Context,
                 val wordTableDao: WordTableDao) :
        PagedListAdapter<WordTable, FavouriteVH>(DIFF_CALLBACK) {

    private val pendingItemRemoval = 3000 // 3sec
    // handler for running delayed runnable
    private val handler = Handler()
    // map of items to pending runnable, so we can cancel a removal if need be
    private val pendingRunnable: MutableMap<WordTable?, Runnable> = HashMap()

    private var itemsPendingRemoval: ArrayList<WordTable?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteVH {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bookmark_list, parent, false)

        return FavouriteVH(view)
    }

    private fun undoOpt(postTable: WordTable?) {
        val pendingRemovalRunnable = pendingRunnable[postTable]
        pendingRunnable.remove(postTable)
        if (pendingRemovalRunnable != null)
            handler.removeCallbacks(pendingRemovalRunnable)

        itemsPendingRemoval.remove(postTable)

        val id = currentList?.indexOf(postTable) ?: 0

        notifyItemChanged(id)
    }

    private fun deletePost(model: WordTable?) {

        val thread = HandlerThread("Bookmark")
        thread.start()
        Handler(thread.looper).post {
            if (model != null) {
                //book mark
                val delete = wordTableDao.delete(model)

                Handler(Looper.getMainLooper()).post {
                    if (delete > 0) {
                        Toasty.info(context, "Item Removed", Toast.LENGTH_SHORT, true).show()
                        //holder.bookmarkImg.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark))
                        if (itemsPendingRemoval.contains(model)) {
                            itemsPendingRemoval.remove(model)
                        }
                    }
                }
            }

            thread.quitSafely()
        }
    }

    fun pendingRemoval(position: Int) {

        val data: WordTable? = getItem(position)
        if (!itemsPendingRemoval.contains(data)) {
            itemsPendingRemoval.add(data)
            // this will redraw row in "undo" state
            notifyItemChanged(position)
            // let's create, store and post a runnable to remove the data
            val pendingRemovalRunnable = Runnable {
                remove(currentList?.indexOf(data) ?: 0)
            }
            handler.postDelayed(pendingRemovalRunnable, pendingItemRemoval.toLong())
            pendingRunnable[data] = pendingRemovalRunnable
        }
    }

    private fun remove(position: Int) {
        val data = currentList?.get(position)

        if (itemsPendingRemoval.contains(data)) {
            itemsPendingRemoval.remove(data)
        }

        if (currentList?.contains(data) == true) {
            deletePost(data)
        }
    }

    fun isPendingRemoval(position: Int): Boolean {
        val data = currentList?.get(position)
        return itemsPendingRemoval.contains(data)
    }

    override fun onBindViewHolder(holder: FavouriteVH, position: Int) {

        val model: WordTable? = getItem(position)

        model?.let {
            //hide fav icon
            holder.favIcon.gone()

            if (itemsPendingRemoval.contains(model)) {
                holder.regular.gone()
                holder.swipe.show()

                holder.undo.setOnClickListener { undoOpt(model) }

            } else {
                holder.swipe.gone()
                holder.regular.show()

                holder.bind(model)

                holder.itemView.tag = model
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(Intent.EXTRA_TEXT, model.id)
                context.startActivity(intent)
            }
        }
    }
}