/*
 * Developed By Shudipto Trafder
 *  on 10/30/18 7:59 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.layout
import kotlinx.android.synthetic.main.app_bar_main.*


class AcknowledgeSheet : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = MyBottomSheetDialog(context!!, theme)

        val view = LayoutInflater.from(context)
                .inflate(R.layout.acknowledgment, mainLay, false)

        dialog.setContentView(view)

        val b = BottomSheetBehavior.from(view.parent as View)

        b?.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
    }

}


/**
 * Expand bottom sheet dialog
 * and control the size
 */
class MyBottomSheetDialog(context: Context, style: Int) : BottomSheetDialog(context, style) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val displayMetrics = DisplayMetrics()

        window?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        when {
            width >= 1400 -> window?.layout(1400)
            width >= 1200 -> window?.layout(1200)
            width >= 1000 -> window?.layout(1000)
        }
    }
}