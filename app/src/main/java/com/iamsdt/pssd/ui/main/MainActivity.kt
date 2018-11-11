/*
 * Developed By Shudipto Trafder
 * on 8/17/18 12:49 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.database.WordTable
import com.iamsdt.pssd.ext.*
import com.iamsdt.pssd.ui.AppAboutActivity
import com.iamsdt.pssd.ui.DeveloperActivity
import com.iamsdt.pssd.ui.add.AddActivity
import com.iamsdt.pssd.ui.callback.ClickListener
import com.iamsdt.pssd.ui.color.ColorActivity
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.details.DetailsActivity
import com.iamsdt.pssd.ui.favourite.FavouriteActivity
import com.iamsdt.pssd.ui.flash.FlashCardActivity
import com.iamsdt.pssd.ui.search.MySuggestionProvider
import com.iamsdt.pssd.ui.settings.SettingsActivity
import com.iamsdt.pssd.utils.Bookmark
import com.iamsdt.pssd.utils.Constants.Companion.PrivacyPolices
import com.iamsdt.pssd.utils.RestoreData
import com.iamsdt.pssd.utils.SettingsUtils
import com.iamsdt.pssd.utils.sync.SyncTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_details.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        ClickListener {

    private val syncTask: SyncTask by inject()

    private val settingsUtils: SettingsUtils by inject()

    private val viewModel: MainVM by viewModel()

    private val restoreData: RestoreData by inject()

    private var suggestions: SearchRecentSuggestions? = null

    private val themeRequestCode = 121

    private lateinit var mSearchView: SearchView

    private var twoPenUI: Boolean = false

    private var mQuery: String = ""

    //share
    private lateinit var shareActionProvider: ShareActionProvider

    //menu
    private lateinit var menuItem: MenuItem

    private var isBookmarked = false

    var word = "Word"
    var des = "des"

    //text size
    var size = 18F

    var id = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainRcv.layoutManager = LinearLayoutManager(this)
        val adapter = MainAdapter(this)
        mainRcv.adapter = adapter

//        val deco = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
//        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//        deco.setDrawable(getDrawable(dercoration))
//
//        // Complete: 8/22/18 add item decoration
//        mainRcv.addItemDecoration(deco)

        viewModel.liveData.observe(this, Observer {
            adapter.submitList(it)
        })

        //for two pen ui
        if (findViewById<FrameLayout>(R.id.details_container) != null) {
            twoPenUI = true
            Timber.i("ID:$id")
            viewModel.singleWord.observe(this, Observer {
                it?.let(::detailsUI)
            })
        }

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


        viewModel.searchEvent.observe(this, Observer {
            if (it != null) {
                if (twoPenUI) {
                    detailsUI(it)
                } else {
                    Timber.i("Status is true")

                    //this method is called two times
                    // don't know why?
                    if (detailsActivity) {
                        val intent = Intent(this, DetailsActivity::class.java)
                        intent.putExtra(Intent.EXTRA_TEXT, it.id)
                        detailsActivity = false

                        //save recent query
                        setRecentQuery(it.word)

                        startActivity(intent)
                    }
                }
            } else {
                showToast(ToastType.WARNING, "Word not found")
                if (::mSearchView.isInitialized) {
                    searchView.setQuery(mQuery, false)
                }
            }
        })

        fab.setOnClickListener {
            // complete: 8/22/18 add random layout
            val dialog = RandomDialog()
            dialog.show(supportFragmentManager, "Random")

            //rand shown
            val ana = FirebaseAnalytics.getInstance(this@MainActivity)
            val bundle = Bundle()
            bundle.putString("search", "Random data shown")
            ana.logEvent("Random_Data", bundle)
        }

        suggestions = SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
        handleSearch(intent)

        setupSearchView()

        //show notification
        getRemoteDataStatus(this, packageName)

        //restore data
        restoreData()

        val toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager

                manager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
        detailsActivity = true
    }


    //set search view
    private fun setupSearchView() {
        mSearchView = searchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        mSearchView.isQueryRefinementEnabled = true

        mSearchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                Timber.i("call")
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                Timber.i("call")
                val selectedView = searchView.suggestionsAdapter
                val cursor = selectedView.getItem(position) as Cursor
                val index = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1)
                searchView.setQuery(cursor.getString(index), true)
                return true
            }

        })

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.i("call")
                viewModel.submit(query)
                //search data
                mQuery = query ?: ""
                val ana = FirebaseAnalytics.getInstance(this@MainActivity)
                val bundle = Bundle()
                bundle.putString("search", query)
                ana.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.i("call")
                newText?.let {
                    Timber.i("new text is $newText")
                    viewModel.requestSearch(it)
                    mQuery = it
                }

                fab.gone()
                return true
            }

        })

        mSearchView.setOnClickListener {

        }

        if (!isShowKeyboard() && ::mSearchView.isInitialized) {
            mSearchView.clearFocus()
        } else {
            mSearchView.findFocus()
            mSearchView.onActionViewExpanded()
        }
    }

    override fun onItemClick(id: Int) {
        if (twoPenUI) {
            viewModel.getSingleWord(id)
        } else {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(Intent.EXTRA_TEXT, id)
            startActivity(intent)
        }
    }

    /*Details data*/
    private fun detailsUI(wordTable: WordTable) {
        details_word?.addStr(wordTable.word)
        details_des?.addStr(wordTable.des)

        id = wordTable.id
        isBookmarked = wordTable.bookmark
        word = wordTable.word
        des = wordTable.des

        resetSap()

        //update icon
        if (::menuItem.isInitialized) {
            if (isBookmarked) {
                menuItem.setIcon(R.drawable.ic_like_fill)
            } else {
                menuItem.setIcon(R.drawable.ic_like_blank)
            }
        }
    }

    /*Restore data*/
    private fun restoreData() {
        RestoreData.ioStatus.observe(this, Observer {
            it?.let { model ->
                //only one type provide
                showToast(ToastType.SUCCESSFUL, model.message)
            }
        })
        restoreDataHelper(restoreData, this)
    }

    private fun setRecentQuery(query: String) {
        suggestions?.saveRecentQuery(query, null)
        mQuery = query
    }

    override fun onNewIntent(intent: Intent) {
        handleSearch(intent)
        Timber.i("voice search")
    }

    private fun handleSearch(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            // complete: 6/14/2018 search
            viewModel.submit(query)

            Timber.i("Search Called")

            mQuery = query

            ////debug only 11/11/2018 remove later
            //Search
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Search Data")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "User search query")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search $query")
            FirebaseAnalytics.getInstance(this)
                    .logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
        }
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            fab.isGone -> {
                fab.show()
            }
            else -> super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        syncTask.initialize(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val actiontext = menu.findItem(R.id.action_txt)
        val shareMenu = menu.findItem(R.id.share)
        menuItem = menu.findItem(R.id.action_favourite)

        if (!twoPenUI) {
            actiontext.isVisible = false
            menuItem.isVisible = false
            shareMenu.isVisible = false
        } else {
            if (isBookmarked)
                menuItem.setIcon(R.drawable.ic_like_fill)

            shareActionProvider = MenuItemCompat.getActionProvider(shareMenu) as ShareActionProvider
            shareActionProvider.setShareIntent(createShareIntent(word, des))
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_favourite ->
                viewModel.requestBookmark(id, isBookmarked)

            R.id.action_clear -> {
                suggestions?.clearHistory()
            }

            R.id.action_txt -> {
                textIncrease()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /*
     Increase text size on tablet mode
     */
    private fun textIncrease() {
        size++
        details_des.textSize = size
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_favourite -> {
                toNextActivity(FavouriteActivity::class)
            }
            R.id.nav_card -> {
                toNextActivity(FlashCardActivity::class)
            }
            R.id.nav_add -> {
                toNextActivity(AddActivity::class)
            }
            R.id.nav_settings -> {
                toNextActivity(SettingsActivity::class)
            }
            R.id.nav_themes -> {
                startActivityForResult(ColorActivity.createIntent(this),
                        themeRequestCode)
            }
            R.id.nav_notice -> {
                val dialog = AcknowledgeSheet()
                dialog.show(supportFragmentManager, "acknowledge")
            }
            R.id.nav_police -> {
                customTab(PrivacyPolices)
            }
            R.id.nav_use -> {
                val dialog = TmsSheet()
                dialog.show(supportFragmentManager, "tms")
            }
            R.id.nav_about -> {
                toNextActivity(AppAboutActivity::class)
            }
            R.id.nav_developer -> {
                toNextActivity(DeveloperActivity::class)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    //recreate the activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == themeRequestCode && resultCode == Activity.RESULT_OK) {
            recreate()
        }
    }


    private fun isShowKeyboard() = settingsUtils.searchIcon

    companion object {
        var isShown = true

        var detailsActivity = true
    }
}
