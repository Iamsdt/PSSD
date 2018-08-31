/*
 * Developed By Shudipto Trafder
 *  on 8/27/18 11:18 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.customTab
import com.iamsdt.pssd.ext.sendEmail
import com.iamsdt.pssd.ui.color.ThemeUtils
import kotlinx.android.synthetic.main.activity_developer.*
import kotlinx.android.synthetic.main.content_developer.*


class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_developer)
        setSupportActionBar(toolbar)

        // TODO: 8/30/18 update link

        //Todo update name pssd

        val subject = "Contact from pssd"

        //dev 1
        dev_fb.click("https://www.facebook.com/iamsdt/")
        dev2_ln.click("https://www.linkedin.com/in/iamsdt")
        dev_git.click("https://github.com/Iamsdt")
        dev_em.setOnClickListener {
            sendEmail("Shudiptotrafder@gmail.com",
                    subject)
        }

        // TODO: 8/31/18 add dev 2

        //dev 2
        dev2_fb.click("")
        dev2_ln.click("")
        dev2_git.click("")
        dev2_em.setOnClickListener {
            sendEmail("", subject)
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun AppCompatImageButton.click(url: String) {
        this.setOnClickListener {
            customTab(url)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


}
