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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.androidextension.gone
import com.iamsdt.androidextension.show
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.utils.SwipeUtil
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.content_favourite.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteActivity : AppCompatActivity() {

    private val adapter: FavouriteAdapter by inject()

    private val viewModel: FavouriteVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_favourite)
        setSupportActionBar(toolbar)

        fav_rcv.layoutManager = LinearLayoutManager(this)

        fav_rcv.adapter = adapter

        // complete: 8/21/18 add empty view
        emptyView()

//        val deco = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//        deco.setDrawable(getDrawable(R.drawable.dercoration))
//
//        // Complete: 8/22/18 add item decoration
//        fav_rcv.addItemDecoration(deco)
        setSwipeForRecyclerView(fav_rcv)


        viewModel.getData().observe(this@FavouriteActivity, Observer {
            if (it.isNotEmpty()) {
                regularView()
                adapter.submitList(it)
            } else {
                emptyView()
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun regularView() {
        regular_view.show()
        empty_view.gone()
    }

    private fun emptyView() {
        regular_view.gone()
        empty_view.show()
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
        //swipeHelper.leftSwipeLabel = "Bookmark removed"
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