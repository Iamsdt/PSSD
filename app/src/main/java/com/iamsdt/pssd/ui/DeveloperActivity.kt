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
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.customTab
import com.iamsdt.pssd.ext.sendEmail
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.color.ThemeUtils
import kotlinx.android.synthetic.main.activity_developer.*
import kotlinx.android.synthetic.main.fragment_developer.*


class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_developer)
        setSupportActionBar(toolbar)


        val subject = "Contact from Soil Science Dictionary"

        //dev 1
        fb1.click("https://www.facebook.com/iamsdt/")
        linkedin1.click("https://www.linkedin.com/in/iamsdt")
        github1.click("https://github.com/Iamsdt")
        email1.setOnClickListener {
            sendEmail("Shudiptotrafder@gmail.com", subject)
        }


        //todo update dev 2
        //dev 2
        fb2.click("https://www.facebook.com/md.rimon.395017")
        linkedin2.click("https://www.linkedin.com/md-mahabubur-rahaman-rimon-2a3708142")
        github2.setOnClickListener {
            dummyToast()
        }
        email2.setOnClickListener {
            sendEmail("mdrimon.kuss@gmail.com", subject)
        }

        //dev 3
        fb3.click("https://www.facebook.com/md.rimon.395017")
        linkedin3.click("https://www.linkedin.com/md-mahabubur-rahaman-rimon-2a3708142")
        github3.setOnClickListener {
            dummyToast()
        }
        email3.setOnClickListener {
            sendEmail("mdrimon.kuss@gmail.com", subject)
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun dummyToast(){
        showToast(ToastType.INFO,"No link found")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    //click method for image button
    private fun AppCompatImageButton.click(url: String) {
        this.setOnClickListener {
            customTab(url)
        }
    }

}
