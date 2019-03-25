/*
 * Developed By Shudipto Trafder
 *  on 8/27/18 11:18 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.dev

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.customTab
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.utils.model.DevModel
import kotlinx.android.synthetic.main.activity_developer.*


class DeveloperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_developer)
        setSupportActionBar(toolbar)

        dev_rcv.layoutManager = LinearLayoutManager(this)
        val adapter = DevAdapter(this,fillData())
        dev_rcv.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun fillData() = listOf(
            DevModel("Shudipto", "Learner, Programmer, Dreamer",
                    "Working as Android App Developer",
                    "Developer",
                    "https://www.facebook.com/iamsdt/",
                    "https://www.linkedin.com/in/iamsdt",
                    "https://github.com/Iamsdt",
                    "Shudiptotrafder@gmail.com")
    )

    private fun dummyToast() {
        showToast(ToastType.INFO, "No link found")
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
