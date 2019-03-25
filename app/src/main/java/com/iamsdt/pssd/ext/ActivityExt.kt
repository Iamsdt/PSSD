/*
 * Developed By Shudipto Trafder
 * on 8/17/18 10:54 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ext

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.iamsdt.pssd.R
import kotlin.reflect.KClass


fun AppCompatActivity.runThread(timer: Long, clazz: KClass<out AppCompatActivity>) =
        Thread {
            try {
                Thread.sleep(timer)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                toNextActivity(clazz,finish = true)
            }
        }.start()

fun AppCompatActivity.toNextActivity(
        clazz: KClass<out AppCompatActivity>,
        extraKey: String = "",
        extra: String = "",
        finish: Boolean = false) {
    val intent = Intent(this, clazz.java)

    if (extraKey.isNotEmpty()) {
        intent.putExtra(extraKey, extra)
    }

    startActivity(intent)

    if (finish) {
        finish()
    }
}

fun Fragment.toNextActivity(
        clazz: KClass<out AppCompatActivity>,
        extraKey: String = "",
        extra: String = "") {

    val intent = Intent(context, clazz.java)

    if (extraKey.isNotEmpty()) {
        intent.putExtra(extraKey, extra)
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


