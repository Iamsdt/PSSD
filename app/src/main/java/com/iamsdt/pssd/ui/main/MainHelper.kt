/*
 * Developed By Shudipto Trafder
 *  on 11/11/18 8:50 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.app.AlertDialog
import android.app.PendingIntent
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.R.id.fab
import com.iamsdt.pssd.R.id.searchView
import com.iamsdt.pssd.ext.gone
import com.iamsdt.pssd.utils.RestoreData
import kotlinx.android.synthetic.main.app_bar_main.*
import timber.log.Timber

/**
 * Add remote data and notify user
 * @param context for notification and intent
 * @param packageName channel id
 */
fun getRemoteDataStatus(context: Context, packageName: String) {
    WorkManager.getInstance()
            .getWorkInfosByTag("Download")
            .get()?.let {
                if (it.isNotEmpty() && it[0].state.isFinished && !MainActivity.isShown) {

                    val builder = NotificationCompat
                            .Builder(context, packageName)
                    builder.setContentTitle("New Data")
                    builder.setContentText("Data added remotely")
                    builder.priority = NotificationCompat.PRIORITY_DEFAULT
                    builder.setSmallIcon(R.drawable.ic_019_information_button,
                            NotificationCompat.PRIORITY_DEFAULT)

                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra(Intent.EXTRA_TEXT, true)

                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val pendingIntent = PendingIntent.getActivity(context,
                            0, intent, 0)

                    builder.setContentIntent(pendingIntent)
                    builder.setAutoCancel(true)

                    val manager =
                            NotificationManagerCompat.from(context)
                    manager.notify(121, builder.build())

                    //shown
                    MainActivity.isShown = true
                }

            }
}

/**
 * Restore data helper
 * just show a user choice dialog
 * and restore process start command
 *
 * @param restoreData Helper class for restore option
 * @param context for dialog
 */
fun restoreDataHelper(restoreData: RestoreData, context: Context) {

    if (restoreData.isFound()) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Restore")
        builder.setMessage("you have some backup data." +
                "\nDo you want to restore it?")

        builder.setPositiveButton("Yes") { _, _ ->
            restoreData.restoreFile()
        }

        builder.setNegativeButton("No") { _, _ ->
            //nothing to do
        }

        val dialog = builder.create()
        dialog.show()
    }
}


/**
 * Create share intent
 * @param word word for share
 * @param des description of the word
 * @return new intent
 */
fun createShareIntent(word: String, des: String): Intent? {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    val share = "$word:$des"
    shareIntent.putExtra(Intent.EXTRA_TEXT, share)
    return shareIntent
}