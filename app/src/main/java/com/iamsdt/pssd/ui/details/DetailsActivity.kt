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
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.addStr
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ext.toNextActivity
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.settings.SettingsActivity
import com.iamsdt.pssd.utils.Bookmark
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DetailsActivity : AppCompatActivity(), TextToSpeech.OnInitListener {


    private val viewModel: DetailsVM by viewModel()

    var id = 0

    private lateinit var shareActionProvider: ShareActionProvider

    private lateinit var menuItem: MenuItem

    private var isBookmarked = false

    var word = "Word"
    var des = "des"

    private lateinit var textToSpeech: TextToSpeech

    var size = 18F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        id = intent.getIntExtra(Intent.EXTRA_TEXT, 0)

        textToSpeech = TextToSpeech(this, this)

        viewModel.getWord(id).observe(this, Observer {
            it?.let {
                details_word.addStr(it.word)
                details_des.addStr(it.des)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    details_des.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
                }

                //save
                word = it.word
                des = it.des

                //reset share action provider
                resetSap()

                isBookmarked = it.bookmark
            }
        })

        viewModel.singleLiveEvent.observe(this, Observer {
            if (::menuItem.isInitialized) {
                it?.let {
                    isBookmarked = when (it) {
                        Bookmark.SET -> {
                            showToast(ToastType.SUCCESSFUL, "Bookmarked")
                            menuItem.setIcon(R.drawable.ic_like_fill)
                            true
                        }

                        Bookmark.DELETE -> {
                            showToast(ToastType.INFO, "Bookmark removed")
                            menuItem.setIcon(R.drawable.ic_like_blank)
                            false
                        }
                    }
                }
            }
        })


        fab.setOnClickListener {
            speakOut()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun speakOut() {

        if (!::textToSpeech.isInitialized) {
            showToast(ToastType.ERROR, "Can not speak right now. Try again")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)

        } else {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.details, menu)

        menuItem = menu.findItem(R.id.action_favourite)

        if (isBookmarked)
            menuItem.setIcon(R.drawable.ic_like_fill)

        val shareMenu = menu.findItem(R.id.share)
        shareActionProvider = MenuItemCompat.getActionProvider(shareMenu) as ShareActionProvider
        shareActionProvider.setShareIntent(createShareIntent())
        return super.onCreateOptionsMenu(menu)
    }

    private fun createShareIntent(): Intent? {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val share = "$word:$des"
        shareIntent.putExtra(Intent.EXTRA_TEXT, share)
        return shareIntent
    }

    private fun resetSap() {
        if (::shareActionProvider.isInitialized) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val link = ""
            val share = "$word:$des -> ${getString(R.string.app_name)}" +
                    "Gplay-$link"
            shareIntent.putExtra(Intent.EXTRA_TEXT, share)
            shareActionProvider.setShareIntent(shareIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
        //back to home
            android.R.id.home -> onBackPressed()

            R.id.action_favourite ->
                viewModel.requestBookmark(id, isBookmarked)

            R.id.action_settings -> {
                toNextActivity(SettingsActivity::class)
            }

            R.id.action_txt -> {
                textIncrease()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun textIncrease() {
        size++
        details_des.textSize = size
    }


}
