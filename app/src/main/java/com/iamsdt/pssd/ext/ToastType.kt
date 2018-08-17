/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:55 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ext

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty

enum class ToastType{
    INFO,
    ERROR,
    SUCCESSFUL,
    WARNING
}

fun AppCompatActivity.showToast(
        type: ToastType,
        message: String,
        time:Int = Toast.LENGTH_SHORT,
        withIcon:Boolean = true){

    when (type) {
        ToastType.INFO -> Toasty.info(this,message,time,withIcon).show()
        ToastType.ERROR -> Toasty.error(this,message,time,withIcon).show()
        ToastType.SUCCESSFUL -> Toasty.success(this,message,time,withIcon).show()
        ToastType.WARNING -> Toasty.warning(this,message,time,withIcon).show()
    }
}

fun Fragment.showToast(
        type: ToastType,
        message: String,
        time:Int = Toast.LENGTH_SHORT,
        withIcon:Boolean = true){

    when (type) {
        ToastType.INFO -> Toasty.info(context!!,message,time,withIcon).show()
        ToastType.ERROR -> Toasty.error(context!!,message,time,withIcon).show()
        ToastType.SUCCESSFUL -> Toasty.success(context!!,message,time,withIcon).show()
        ToastType.WARNING -> Toasty.warning(context!!,message,time,withIcon).show()
    }
}