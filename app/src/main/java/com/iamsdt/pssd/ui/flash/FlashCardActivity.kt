/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 6:00 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.flash

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.gone
import com.iamsdt.pssd.ext.show
import com.iamsdt.pssd.ui.callback.ClickListener
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.favourite.FavouriteVM
import kotlinx.android.synthetic.main.activity_flash_card.*
import kotlinx.android.synthetic.main.content_flash_card.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FlashCardActivity : AppCompatActivity(), ClickListener {

    private val viewModel: FavouriteVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_flash_card)
        setSupportActionBar(toolbar)

        val manager = GridLayoutManager(this, 2,
                RecyclerView.VERTICAL, false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager.spanCount = 3
        }

        flash_rcv.layoutManager = manager

        val adapter = FlashAdapter(this)

        flash_rcv.adapter = adapter

        viewModel.getData().observe(this@FlashCardActivity, Observer { list ->
            list?.let {
                if (it.isNotEmpty()) {
                    regularView()
                    adapter.submitList(it)
                } else {
                    emptyView()
                }
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

    override fun onItemClick(id: Int) {
        Timber.i("Tag rec: $id")
        val dialog = FlashSheet()
        dialog.show(supportFragmentManager, id.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}