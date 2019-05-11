/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:54 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ext

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.iamsdt.pssd.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun <reified T : AppCompatActivity> AppCompatActivity.nextActivity(
        extraKey: String = "", extra: Any = "", finish: Boolean = false
) {

    val intent = Intent(this, T::class.java)

    if (extraKey.isNotEmpty()) {
        when (extra) {
            is String -> intent.putExtra(extraKey, extra)
            is Boolean -> intent.putExtra(extraKey, extra)
            is Float -> intent.putExtra(extraKey, extra)
            is Long -> intent.putExtra(extraKey, extra)
            is Int -> intent.putExtra(extraKey, extra)
            is Bundle -> intent.putExtra(extraKey, extra)
            else -> intent.putExtra(extraKey, "$extra")
        }
    }

    startActivity(intent)

    if (finish) finish()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.runThreadK(timer: Long = 1000) {
    GlobalScope.launch {
        delay(timer)
        nextActivity<T>()
        finish()
    }
}

inline fun <reified T : AppCompatActivity> Fragment.nextActivity(
        extraKey: String = "", extra: Any = ""
) {
    val intent = Intent(activity, T::class.java)

    if (extraKey.isNotEmpty()) {
        when (extra) {
            is String -> intent.putExtra(extraKey, extra)
            is Boolean -> intent.putExtra(extraKey, extra)
            is Float -> intent.putExtra(extraKey, extra)
            is Long -> intent.putExtra(extraKey, extra)
            is Int -> intent.putExtra(extraKey, extra)
            is Bundle -> intent.putExtra(extraKey, extra)
            else -> intent.putExtra(extraKey, "$extra")
        }
    }

    startActivity(intent)
}


fun AppCompatActivity.customTab(link: String) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(R.attr.colorPrimary)
    builder.setShowTitle(true)
    builder.addDefaultShareMenuItem()
    //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
    //resources, R.drawable.dialog_back))
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(link))
}

fun AppCompatActivity.sendEmail(
        email: String,
        subject: String) {

    val intent = Intent(Intent.ACTION_SENDTO)
    intent.type = "text/plain"
    intent.data = Uri.parse("mailto:$email")
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    startActivity(Intent.createChooser(intent, "Send Email"))
}


