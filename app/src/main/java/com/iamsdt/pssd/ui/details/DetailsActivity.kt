/*
 * Developed By Shudipto Trafder
 * on 8/17/18 5:59 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.ViewModelFactory
import com.iamsdt.pssd.ext.addStr
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.utils.Bookmark
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: DetailsVM by lazy {
        ViewModelProviders.of(this, factory).get(DetailsVM::class.java)
    }

    var id = 0

    private lateinit var shareActionProvider: ShareActionProvider

    private lateinit var menuItem: MenuItem

    private var isBookmarked = false

    var word = "Word"
    var des = "des"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        id = intent.getIntExtra(Intent.EXTRA_TEXT, 0)


        viewModel.getWord(id).observe(this, Observer {
            it?.let {
                details_word.addStr(it.word)
                details_des.addStr(it.des)

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
                            showToast(ToastType.WARNING, "Bookmark removed")
                            menuItem.setIcon(R.drawable.ic_like_blank)
                            false
                        }
                    }
                }
            }
        })


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
                //toNextActivity(SettingsActivity::class)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun textIncrease() {
        // TODO: 8/17/2018 add increase option from settings
    }


}
