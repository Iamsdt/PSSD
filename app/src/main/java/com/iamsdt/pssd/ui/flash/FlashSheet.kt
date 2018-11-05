/*
 * Developed By Shudipto Trafder
 *  on 8/22/18 10:35 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.flash

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.addStr
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.main.MyBottomSheetDialog
import com.iamsdt.pssd.utils.TxtHelper
import kotlinx.android.synthetic.main.activity_flash_card.*
import kotlinx.android.synthetic.main.flash_sheet.view.*
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*


class FlashSheet : BottomSheetDialogFragment(), TextToSpeech.OnInitListener {

    private val wordTableDao: WordTableDao by inject()

    private val txtHelper: TxtHelper by inject()

    private lateinit var textToSpeech: TextToSpeech

    private var wordTxt = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = MyBottomSheetDialog(context!!, theme)

        val view = View.inflate(context, R.layout.flash_sheet, flashLay)
        dialog.setContentView(view)

        val b = BottomSheetBehavior.from(view.parent as View)

        b?.state = BottomSheetBehavior.STATE_EXPANDED

        textToSpeech = TextToSpeech(context, this)

        val wordTv: TextView = view.wordTV
        val desTV: TextView = view.desTV
        val speakBtn: ImageButton = view.speak

        val id = tag?.toInt() ?: 0

        Timber.i("Tag: $id")

        //draw ui
        wordTableDao.getSingleWord(id).observe(this, Observer { table ->
            table?.let {
                wordTv.addStr(it.word)
                desTV.addStr(it.des)

                txtHelper.setSize(wordTv, desTV)

                //save word
                wordTxt = it.word

            }
        })


        speakBtn.setOnClickListener {
            speakOut()
        }

        return dialog
    }

    private fun speakOut() {

        if (!::textToSpeech.isInitialized) {
            showToast(ToastType.ERROR, "Can not speak right now. Try again")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(wordTxt, TextToSpeech.QUEUE_FLUSH, null, null)

        } else {
            @Suppress("DEPRECATION")
            textToSpeech.speak(wordTxt, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.ERROR && ::textToSpeech.isInitialized) {

            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED ||
                    result == TextToSpeech.ERROR_NOT_INSTALLED_YET) {

                val installIntent = Intent()
                installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                startActivity(installIntent)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
}

