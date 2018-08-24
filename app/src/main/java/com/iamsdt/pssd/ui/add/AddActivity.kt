/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 9:25 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.add

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.main.MainAdapter
import com.iamsdt.pssd.utils.Constants.ADD.DIALOG
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddActivity : AppCompatActivity() {

    val model: AddVM by viewModel()

    lateinit var dialog: AddWordDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        // TODO: 8/24/18 add empty view

        addRcv.layoutManager = LinearLayoutManager(this)
        val adapter = MainAdapter(this)
        addRcv.adapter = adapter

        val deco = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        deco.setDrawable(getDrawable(R.drawable.dercoration))
        addRcv.addItemDecoration(deco)

        model.getWord().observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        model.activityStatus.observe(this, Observer {
            it?.let {
                if (it.status && it.title == DIALOG) {
                    if (::dialog.isInitialized && dialog.isVisible) {
                        dialog.dismiss()
                        showToast(ToastType.SUCCESSFUL, it.message)
                    }
                }
            }
        })

        dialog.dismiss()

        fab.setOnClickListener {
            dialog = AddWordDialog()
            dialog.show(supportFragmentManager, "Add word")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
