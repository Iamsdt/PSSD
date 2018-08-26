/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 9:25 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.main.MainAdapter
import com.iamsdt.pssd.utils.Constants.ADD.DES
import com.iamsdt.pssd.utils.Constants.ADD.DIALOG
import com.iamsdt.pssd.utils.Constants.ADD.WORD
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.add_dialog.view.*
import kotlinx.android.synthetic.main.content_add.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddActivity : AppCompatActivity() {

    val model: AddVM by viewModel()

    lateinit var dialog: AlertDialog

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

        model.dialogStatus.observe(this, Observer {
            it?.let {
                if (it.status && it.title == DIALOG) {
                    if (::dialog.isInitialized && dialog.isShowing) {
                        dialog.dismiss()
                        showToast(ToastType.SUCCESSFUL, it.message)

                        //add analytics
                        val ana = FirebaseAnalytics.getInstance(this@AddActivity)
                        val bundle = Bundle()
                        bundle.putString("Data_added","Data Put in the local database")
                        ana.logEvent("add_data",bundle)
                    }
                }
            }
        })

        fab.setOnClickListener {
            showDialog()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("InflateParams")
    private fun showDialog() {
        val view = LayoutInflater.from(this)
                .inflate(R.layout.add_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val wordTV: TextInputLayout = view.add_word
        val desTV: TextInputLayout = view.add_des
        val button: AppCompatImageButton = view.add_btn

        button.setOnClickListener {
            val word = wordTV.editText?.text ?: ""
            val des = desTV.editText?.text ?: ""

            model.addData(word.toString(), des.toString())
        }

        model.dialogStatus.observe(this, Observer {
            it?.let {
                if (!it.status && it.title == WORD) {
                    wordTV.error = it.message
                } else if (!it.status && it.title == DES) {
                    desTV.error = it.message
                }
            }
        })

        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
