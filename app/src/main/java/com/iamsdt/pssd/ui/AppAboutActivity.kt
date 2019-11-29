/*
 * Developed By Shudipto Trafder
 *  on 8/26/18 5:54 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ui.color.ThemeUtils
import kotlinx.android.synthetic.main.activity_app_about.*
import kotlinx.android.synthetic.main.content_app_about.*

class AppAboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_app_about)
        setSupportActionBar(toolbar)


        //complete about
        about_git.setOnClickListener {
            customTab("https://github.com/Iamsdt/SoilScienceDictionary")
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun AppCompatActivity.customTab(link: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(R.attr.colorPrimary)
        builder.setShowTitle(true)
        builder.addDefaultShareMenuItem()
        //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
        //resources, R.drawable.dialog_back))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(link))
    }

}
