/*
 * Developed By Shudipto Trafder
 * on 8/17/18 11:08 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iamsdt.androidextension.ToastType
import com.iamsdt.androidextension.showToasty
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.utils.FileImportExportUtils
import kotlinx.android.synthetic.main.activity_settings_backup.*

class BackupSettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_settings_backup)
        setSupportActionBar(toolbar)
        toolbar.elevation = 0f


        //boiler plate code
        FileImportExportUtils.ioStatus.observe(this, Observer { model ->
            model?.let {
                if (!isShown) {
                    isShown = if (it.status) {
                        showToasty( it.message,ToastType.SUCCESSFUL)
                        true
                    } else {
                        showToasty(it.message, ToastType.ERROR, Toast.LENGTH_LONG)
                        true
                    }
                }
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        var isShown = false
    }

}
