/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 5:59 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.favourite

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ViewModelFactory
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.content_favourite.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class FavouriteActivity:AppCompatActivity(){

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

        fav_rcv.layoutManager = LinearLayoutManager(this)
        val adapter = FavouriteAdapter(this)
        mainRcv.adapter = adapter
        viewModel.data.observe(this, Observer {
            adapter.submitList(it)
        })


        viewModel.singleLiveEvent.observe(this, Observer {
            //bookmark
            // TODO: 8/21/18 add bookmark
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}