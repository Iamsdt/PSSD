/*
 * Developed By Shudipto Trafder
 *  on 11/11/18 8:50 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.utils.RestoreData
import com.iamsdt.pssd.utils.SpUtils

/**
 * Restore data helper
 * just show a user choice dialog
 * and restore process start command
 *
 * @param restoreData Helper class for restore option
 * @param context for dialog
 */
fun restoreDataHelper(restoreData: RestoreData, context: Context, spUtils: SpUtils) {

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
            spUtils.restore = true
        }

        val dialog = builder.create()
        dialog.show()
    }
}


/**
 * Create share intent
 * @param word word Table
 * @return new intent
 */
fun createShareIntent(word: WordTable): Intent? {
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    val share = "${word.word}:${word.des}"
    shareIntent.putExtra(Intent.EXTRA_TEXT, share)
    return shareIntent
}