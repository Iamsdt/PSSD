package com.iamsdt.pssd.ui.add

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iamsdt.androidextension.ToastType
import com.iamsdt.androidextension.showToasty
import com.iamsdt.pssd.R
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.add_dialog.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InsertActivity : AppCompatActivity() {

    private val model: AddVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        setSupportActionBar(toolbar)

        model.insertStatus.observe(this, Observer {
            if (it != null && it.status) {
                showToasty("Added Successfully", ToastType.SUCCESSFUL)
                finish()
            }
        })

        fab.setOnClickListener {
            val word = add_word?.editText?.text?.toString() ?: ""
            val des = add_des?.editText?.text?.toString() ?: ""
            val ref = add_ref?.editText?.text?.toString() ?: ""
            if (check(word, des, ref)) return@setOnClickListener

            model.addData(word, des, ref)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun check(word: String, des: String, ref: String): Boolean {
        if (word.isEmpty() || word.length < 2) {
            add_word.error = "Please add valid word"
            return true
        }

        if (des.isEmpty() || des.length < 10) {
            add_word.error = "Please add larger description"
            return true
        }

        if (ref.isEmpty() || ref.length < 10) {
            add_word.error = "Please add book name with author"
            return true
        }


        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
