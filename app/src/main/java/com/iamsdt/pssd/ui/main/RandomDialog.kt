package com.iamsdt.pssd.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamsdt.androidextension.MyCoroutineContext
import com.iamsdt.androidextension.ToastType
import com.iamsdt.androidextension.showToasty
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTableDao
import com.iamsdt.pssd.utils.TxtHelper
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.random_sheet.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class RandomDialog : BottomSheetDialogFragment(), TextToSpeech.OnInitListener {

    private val wordTableDao: WordTableDao by inject()

    private val txtHelper: TxtHelper by inject()

    private lateinit var textToSpeech: TextToSpeech

    private var wordTxt = ""

    var size = 100

    var bookmark = false

    private val uiScope = MyCoroutineContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(uiScope)

        uiScope.launch {
            size = withContext(Dispatchers.IO) {
                wordTableDao.getAllList().size
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = MyBottomSheetDialog(context!!, theme)

        val view = View.inflate(context, R.layout.random_sheet, mainLay)
        dialog.setContentView(view)

        val b = BottomSheetBehavior.from(view.parent as View)

        b.state = BottomSheetBehavior.STATE_EXPANDED

        textToSpeech = TextToSpeech(context, this)

        val wordTV: AppCompatTextView = view.wordTV
        val desTV: AppCompatTextView = view.desTV
        val favIcon: ImageButton = view.like
        val speak: ImageButton = view.speak

        val id = getRandomID()

        Timber.i("ID get: $id")

        //draw ui
        wordTableDao.getSingleWord(id).observe(this@RandomDialog, Observer { wordTable ->
            wordTable?.let {
                //save bookmark
                bookmark = wordTable.bookmark

                //save word txt
                wordTxt = wordTable.word

                wordTV.text = (wordTable.word)
                desTV.text = (wordTable.des)

                txtHelper.setSize(wordTV, desTV)

                // complete: 8/17/2018 favourite icon
                if (wordTable.bookmark) {
                    favIcon.setImageDrawable(view.context.getDrawable(R.drawable.ic_like_fill))
                } else {
                    favIcon.setImageDrawable(view.context.getDrawable(R.drawable.ic_like_blank))
                }
            }
        })


        favIcon.setOnClickListener {
            uiScope.launch {
                if (bookmark) {
                    val status = withContext(Dispatchers.IO) {
                        wordTableDao.deleteBookmark(id)
                    }
                    if (status > 0) {
                        showToasty("Bookmark Delete")
                    }
                } else {
                    val status = withContext(Dispatchers.IO) {
                        wordTableDao.setBookmark(id)
                    }
                    if (status > 0) {
                        showToasty("Bookmarked", ToastType.SUCCESSFUL)
                    }
                }
            }
        }


        speak.setOnClickListener {
            speakOut()
        }

        return dialog
    }

    private fun getRandomID(): Int {
        val random = Random()
        var int = random.nextInt(size)

        //rare in case, i found during testing
        if (int == 0) {
            int = 1
        }

        return int
    }

    private fun speakOut() {

        if (!::textToSpeech.isInitialized) {
            showToasty("Can not speak right now. Try again", ToastType.ERROR)
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