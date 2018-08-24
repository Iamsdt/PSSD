/*
 * Developed By Shudipto Trafder
 *  on 8/24/18 9:00 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import com.iamsdt.pssd.R
import com.iamsdt.pssd.utils.Constants.ADD.WORD
import kotlinx.android.synthetic.main.add_dialog.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWordDialog : DialogFragment() {


    val model: AddVM by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.add_dialog, container)

        val wordTV: TextInputLayout = view.add_word
        val desTV: TextInputLayout = view.add_des
        val button: AppCompatImageButton = view.add_btn

        button.setOnClickListener {
            val word = wordTV.editText?.text ?: ""
            val des = desTV.editText?.text ?: ""

            model.addData(word.toString(), des.toString())
        }

        model.fragmentStatus.observe(this, Observer {
            it?.let {
                if (it.title == WORD) {
                    wordTV.error = it.message
                } else {
                    desTV.error = it.message
                }
            }
        })

        return view
    }


}