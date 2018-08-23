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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ViewModelFactory
import com.iamsdt.pssd.ui.favourite.FavouriteVM
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_flash_card.*
import kotlinx.android.synthetic.main.content_flash_card.*
import timber.log.Timber
import javax.inject.Inject

class FlashCardActivity:AppCompatActivity(),HasSupportFragmentInjector,FlashAdapter.ClickListener{

    @Inject
    lateinit var di: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: FavouriteVM by lazy {
        ViewModelProviders.of(this, factory).get(FavouriteVM::class.java)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = di

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeUtils.initialize(this)
        setContentView(R.layout.activity_flash_card)
        setSupportActionBar(toolbar)

        val manager = GridLayoutManager(this,2,
                GridLayoutManager.VERTICAL,false)

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager.spanCount = 3
        }

        flash_rcv.layoutManager = manager

        val adapter = FlashAdapter(this)

        flash_rcv.adapter = adapter

        viewModel.getData().observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun click(id: Int) {
        Timber.i("Tag rec: $id")
        val dialog = FlashSheet()
        dialog.show(supportFragmentManager,id.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}