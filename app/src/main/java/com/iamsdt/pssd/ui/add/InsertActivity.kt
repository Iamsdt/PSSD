package com.iamsdt.pssd.ui.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.showToast
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
                showToast(ToastType.SUCCESSFUL, "Added Successfully")
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

}
