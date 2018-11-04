/*
 * Developed By Shudipto Trafder
 *  on 10/30/18 7:56 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamsdt.pssd.R
import kotlinx.android.synthetic.main.app_bar_main.*

/**
 * Created by Shudipto Trafder on 10/30/2018.
 * at 7:56 PM
 */
class TmsSheet : BottomSheetDialogFragment() {

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = View.inflate(context, R.layout.terms, mainLay)
        dialog.setContentView(view)

        val b = BottomSheetBehavior.from(view.parent as View)

        b?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}