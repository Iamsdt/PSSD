/*
 * Developed By Shudipto Trafder
 *  on 10/30/18 7:56 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamsdt.pssd.R
import kotlinx.android.synthetic.main.app_bar_main.*


class TmsSheet : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = MyBottomSheetDialog(context!!, theme)

        val view = View.inflate(context, R.layout.terms, mainLay)
        dialog.setContentView(view)

        val b = BottomSheetBehavior.from(view.parent as View)

        b.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
    }
}