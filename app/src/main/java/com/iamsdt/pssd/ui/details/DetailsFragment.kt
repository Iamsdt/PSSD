/*
 * Developed By Shudipto Trafder
 *  on 9/22/18 9:40 AM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.details

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Layout
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.addStr
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ext.toNextActivity
import com.iamsdt.pssd.ui.settings.SettingsActivity
import com.iamsdt.pssd.utils.Bookmark
import kotlinx.android.synthetic.main.fragment_details.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import androidx.lifecycle.Observer


/**
 * Created by Shudipto Trafder on 9/22/2018.
 * at 9:40 AM
 */

class DetailsFragment : Fragment(), TextToSpeech.OnInitListener {

    private val viewModel: DetailsVM by viewModel()

    //id word
    private var wordId = 1

    //share
    private lateinit var shareActionProvider: ShareActionProvider

    //menu
    private lateinit var menuItem: MenuItem

    private var isBookmarked = false

    var word = "Word"
    var des = "des"

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var detailsTV: TextView

    //text size
    var size = 18F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        wordId = tag?.toInt() ?: 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_details, container, false)

        textToSpeech = TextToSpeech(context, this)

        viewModel.getWord(wordId).observe(this, Observer { table ->
            table?.let {
                view.details_word.addStr(it.word)
                view.details_des.addStr(it.des)

                detailsTV = view.details_des

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    view.details_des.justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
                }

                //save
                word = it.word
                des = it.des

                //reset share action provider
                resetSap()

                isBookmarked = it.bookmark
            }
        })

        viewModel.singleLiveEvent.observe(this, Observer { bookmark ->
            if (::menuItem.isInitialized) {
                bookmark?.let {
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



        view.fab.setOnClickListener {
            speakOut()
        }


        return view
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.details, menu)

        menuItem = menu.findItem(R.id.action_favourite)

        if (isBookmarked)
            menuItem.setIcon(R.drawable.ic_like_fill)

        val shareMenu = menu.findItem(R.id.share)
        shareActionProvider = MenuItemCompat.getActionProvider(shareMenu) as ShareActionProvider
        shareActionProvider.setShareIntent(createShareIntent())

        return super.onCreateOptionsMenu(menu, inflater)
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
            //todo 9/18/2018 add google play link
            val link = ""
            val share = "$word:$des -> ${getString(R.string.app_name)}" +
                    "Gplay-$link"
            shareIntent.putExtra(Intent.EXTRA_TEXT, share)
            shareActionProvider.setShareIntent(shareIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_favourite ->
                viewModel.requestBookmark(wordId, isBookmarked)

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
        detailsTV.textSize = size
    }

}