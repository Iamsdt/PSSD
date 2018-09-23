/*
 * Developed By Shudipto Trafder
 * on 8/17/18 5:59 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.addStr
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ext.toNextActivity
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.settings.SettingsActivity
import com.iamsdt.pssd.utils.Bookmark
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        val id = intent.getIntExtra(Intent.EXTRA_TEXT, 0)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.details_fragment_container,
                            DetailsFragment(), "$id")
                    .commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            //back to home
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
