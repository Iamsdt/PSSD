/*
 * Developed By Shudipto Trafder
 *  on 10/30/18 7:59 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamsdt.pssd.R

/**
 * Created by Shudipto Trafder on 10/30/2018.
 * at 7:59 PM
 */
class AcknowledgeSheet : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.acknowledgment, container, false)
    }
}