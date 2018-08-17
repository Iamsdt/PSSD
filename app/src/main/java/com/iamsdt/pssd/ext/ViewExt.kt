/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:55 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ext

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun View.gone(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.inVisible(){
    visibility = View.INVISIBLE
}

fun View.changeHeight(height: Int){
    requestLayout()
    layoutParams.height = height
}

fun TextView.addStr(string: String){
    this.text = string
}

fun View.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, snackbarText, timeLength).show()
}