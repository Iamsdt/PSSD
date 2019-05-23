/*
 * Developed By Shudipto Trafder
 *  on 8/27/18 11:18 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.dev

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
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
        val adapter = DevAdapter(this, fillData())
        dev_rcv.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun fillData() = listOf(
            DevModel("Shudipto", "Learner, Programmer, Dreamer",
                    "Work as Android App Developer",
                    "Developer",
                    "https://www.facebook.com/iamsdt/",
                    "https://www.linkedin.com/in/iamsdt",
                    "https://github.com/Iamsdt",
                    "Shudiptotrafder@gmail.com",
                    drawable(1)),
            DevModel("MD. Imran Hossain", "Student",
                    "Work as Icon Designer",
                    "Designer",
                    "https://www.facebook.com/imranhossain.raju.142",
                    "",
                    "",
                    "expendableimran@gmail.com",
                    drawable(2)),
            DevModel("Jannatun Nayeema",
                    "Dreamer, Learner",
                    "Work on Database v2",
                    "Database Developer",
                    "https://www.facebook.com/100027839883903",
                    "",
                    "",
                    "Jannatunnayeema2@gmail.com",
                    drawable(3)),
            DevModel("Shudipto", "Student, Dreamer",
                    "Work on Database v1",
                    "Database Developer",
                    "https://www.facebook.com/md.rimon.395017",
                    "https://www.linkedin.com/in/md-mahabubur-rahaman-rimon-2a3708142/",
                    "",
                    "mdrimon.kuss@gmail.com",
                    drawable(4))
    )

    fun drawable(pos: Int): Drawable {
        val id = when (pos) {
            1 -> R.drawable.sdt_round
            2 -> R.drawable.imran
            3 -> R.drawable.nayeema
            4 -> R.drawable.ri_round
            else -> R.drawable.sdt_round
        }
        @Suppress("DEPRECATION")
        return resources.getDrawable(id)
    }

    private fun dummyToast() {
        showToast(ToastType.INFO, "No link found")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
