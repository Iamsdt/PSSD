/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 5:59 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.favourite

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ViewModelFactory
import com.iamsdt.pssd.utils.SwipeUtil
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.content_favourite.*
import javax.inject.Inject

class FavouriteActivity:AppCompatActivity(){

    @Inject
    lateinit var adapter:FavouriteAdapter

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: FavouriteVM by lazy {
        ViewModelProviders.of(this, factory).get(FavouriteVM::class.java)
    }

    // TODO: 8/21/18 add swap option

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeUtils.initialize(this)
        setContentView(R.layout.activity_favourite)
        setSupportActionBar(toolbar)

        //change the context
        adapter.changeContext(this)

        fav_rcv.layoutManager = LinearLayoutManager(this)

        fav_rcv.adapter = adapter

        // TODO: 8/21/18 add empty view
        //emptyView()

        setSwipeForRecyclerView(fav_rcv)

        viewModel.getData().observe(this, Observer {
            //bookmarkView()
            adapter.submitList(it)

            if (it?.size ?: 0 == 0) {
                //emptyView()
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setSwipeForRecyclerView(recyclerView: RecyclerView) {

        val swipeHelper = object : SwipeUtil(0, ItemTouchHelper.START or ItemTouchHelper.END, this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val swipedPosition = viewHolder.adapterPosition
                val adapter = recyclerView.adapter as FavouriteAdapter
                adapter.pendingRemoval(swipedPosition)
            }

            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val position = viewHolder.adapterPosition
                val adapter = recyclerView.adapter as FavouriteAdapter
                return if (adapter.isPendingRemoval(position)) {
                    0
                } else super.getSwipeDirs(recyclerView, viewHolder)
            }
        }

        val mItemTouchHelper = ItemTouchHelper(swipeHelper)
        mItemTouchHelper.attachToRecyclerView(recyclerView)

        //set swipe label
        swipeHelper.leftSwipeLabel = "Bookmark removed"
        //set swipe background-Color
        swipeHelper.leftColorCode = ContextCompat.getColor(this, R.color.red_300)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}