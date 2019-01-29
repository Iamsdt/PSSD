/*
 * Developed By Shudipto Trafder
 * on 8/17/18 5:59 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Layout
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.addStr
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.main.MainVM
import com.iamsdt.pssd.utils.Bookmark
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class DetailsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {


    private val viewModel: MainVM by viewModel()

    //id word
    var id = 1

    //share
    //private lateinit var shareActionProvider: ShareActionProvider

    private var isBookmarked = false

    var word = "Word"
    var des = "des"

    private lateinit var textToSpeech: TextToSpeech

    //text size
    var size = 20F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_details)
        setSupportActionBar(details_toolbar)

        id = intent.getIntExtra(Intent.EXTRA_TEXT, 1)

        textToSpeech = TextToSpeech(this, this)

        viewModel.getWord(id).observe(this, Observer { table ->
            table?.let {
                details_word.addStr(it.word)
                details_des.addStr(it.des)
                val r = "Reference: ${it.reference}"
                details_ref.addStr(r)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    details_des.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
                }

                //save
                word = it.word
                des = it.des

                //reset share action provider
                //resetSap()

                isBookmarked = it.bookmark
            }
        })

        viewModel.singleLiveEvent.observe(this, Observer { bookmark ->
            bookmark?.let {
                Timber.i("Called")
                isBookmarked = when (it) {
                    Bookmark.SET -> {
                        showToast(ToastType.SUCCESSFUL, "Bookmarked")
                        true
                    }
                    Bookmark.DELETE -> {
                        showToast(ToastType.INFO, "Bookmark removed")
                        false
                    }
                }
                changeLoveIcon(isBookmarked)
            }
        })


        details_speak.setOnClickListener {
            speakOut()
        }

        details_text_size.setOnClickListener {
            textIncrease()
        }

        //change love icon
        changeLoveIcon(isBookmarked)

        details_love.setOnClickListener {
            viewModel.requestBookmark(id, isBookmarked)
        }

        details_share.setOnClickListener {
            //val intent = createShareIntent()
            startActivity(createShareIntent())
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun changeLoveIcon(bookmark: Boolean) {

        val id = if (bookmark) R.drawable.ic_like_fill
        else R.drawable.ic_like_blank

        details_love.setImageDrawable(getDrawable(id))
    }

    private fun speakOut() {

        if (!::textToSpeech.isInitialized) {
            showToast(ToastType.ERROR, "Can not speak right now. Try again")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)

        } else {
            @Suppress("DEPRECATION")
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null)
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.details, menu)
//
//        val shareMenu = menu.findItem(R.id.share)
//        shareActionProvider = MenuItemCompat.getActionProvider(shareMenu) as ShareActionProvider
//        shareActionProvider.setShareIntent(createShareIntent())
//        return super.onCreateOptionsMenu(menu)
//    }

//    private fun createShareIntent(): Intent? {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.type = "text/plain"
//        val share = "$word:$des"
//        shareIntent.putExtra(Intent.EXTRA_TEXT, share)
//        return shareIntent
//    }

    private fun createShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val link = "https://play.google.com/store/apps/details?id=com.iamsdt.pssd"
        val share = "$word:$des -> ${getString(R.string.app_name)}" +
                "Gplay-$link"
        shareIntent.putExtra(Intent.EXTRA_TEXT, share)
        shareIntent.type = "text/plain"
        return shareIntent
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            //back to home
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun textIncrease() {
        size++
        details_des.textSize = size
    }


}
