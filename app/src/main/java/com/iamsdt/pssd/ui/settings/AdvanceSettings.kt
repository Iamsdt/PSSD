/*
 * Developed By Shudipto Trafder
 * on 8/17/18 11:08 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ui.color.ThemeUtils
import kotlinx.android.synthetic.main.activity_settings_advance.*

class AdvanceSettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_settings_advance)
        setSupportActionBar(toolbar)
        toolbar.elevation = 0f

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


}
