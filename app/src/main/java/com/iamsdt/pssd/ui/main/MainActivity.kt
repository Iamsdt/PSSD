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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.R.drawable.dercoration
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.gone
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ext.toNextActivity
import com.iamsdt.pssd.ui.DeveloperActivity
import com.iamsdt.pssd.ui.add.AddActivity
import com.iamsdt.pssd.ui.callback.ClickListener
import com.iamsdt.pssd.ui.color.ColorActivity
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.details.DetailsActivity
import com.iamsdt.pssd.ui.details.DetailsFragment
import com.iamsdt.pssd.ui.favourite.FavouriteActivity
import com.iamsdt.pssd.ui.flash.FlashCardActivity
import com.iamsdt.pssd.ui.search.MySuggestionProvider
import com.iamsdt.pssd.ui.settings.SettingsActivity
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.RestoreData
import com.iamsdt.pssd.utils.SettingsUtils
import com.iamsdt.pssd.utils.sync.SyncTask
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
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

    private lateinit var searchView: androidx.appcompat.widget.SearchView

    private var twoPenUI: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        mainRcv.layoutManager = LinearLayoutManager(this)
        val adapter = MainAdapter(this)
        mainRcv.adapter = adapter

        val deco = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        deco.setDrawable(getDrawable(dercoration))

        // Complete: 8/22/18 add item decoration
        mainRcv.addItemDecoration(deco)

        viewModel.liveData.observe(this, Observer {
            adapter.submitList(it)
        })

        //for two pen ui
        if (findViewById<FrameLayout>(R.id.details_container) != null) {
            twoPenUI = true

            supportFragmentManager.beginTransaction()
                    .replace(R.id.details_container,
                            DetailsFragment(), "1")
                    .commit()
        }

        viewModel.event.observe(this, Observer { model ->
            model?.let {
                if (it.title == Constants.SEARCH) {
                    if (it.status) {
                        Timber.i("Status is true")
                        val intent = Intent(this, DetailsActivity::class.java)
                        intent.putExtra(Intent.EXTRA_TEXT, it.message.toInt())

                        //save recent query
                        setRecentQuery(it.extra)

                        startActivity(intent)
                    } else {
                        Timber.i(it.message)
                        showToast(ToastType.WARNING, it.message)
                        if (::searchView.isInitialized) {
                            searchView.setQuery(it.extra, false)
                        }
                    }
                }
            }
        })

        fab.setOnClickListener { _ ->
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

        //show notification
        getRemoteDataStatus()

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

    override fun onItemClick(id: Int) {
        if (twoPenUI) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.details_container,
                            DetailsFragment(), "$id")
                    .commit()
        } else {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(Intent.EXTRA_TEXT, id)
            startActivity(intent)
        }
    }

    private fun restoreData() {

        RestoreData.ioStatus.observe(this, Observer {
            it?.let { model ->
                //only one type provide
                showToast(ToastType.SUCCESSFUL, model.message)
            }
        })

        if (restoreData.isFound()) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Restore")
            builder.setMessage("you have some backup data." +
                    "\nDo you want to restore it?")

            builder.setPositiveButton("Yes") { _, _ ->
                restoreData.restoreFile()
            }

            builder.setNegativeButton("No") { _, _ ->
                //nothing to do
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun setRecentQuery(query: String) {
        suggestions?.saveRecentQuery(query, null)
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
            !(searchView.isIconified) -> {
                searchView.onActionViewCollapsed()
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

        searchView = menu.findItem(R.id.search)?.actionView as SearchView
        //search
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        //complete  make settings
        if (isIconifiedDefault()) {
            searchView.setIconifiedByDefault(true)
        } else {
            searchView.setIconifiedByDefault(false)
        }

        searchView.isQueryRefinementEnabled = true

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Timber.i("call")
                viewModel.submit(query)
                //search data

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
                }

                fab.gone()
                return true
            }

        })

        searchView.setOnClickListener {


        }

        return true
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
                showDummyMessage()
            }
            R.id.nav_police -> {
                showDummyMessage()
            }
            R.id.nav_use -> {
                showDummyMessage()
            }
            R.id.nav_about -> {
                showDummyMessage()
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

    private fun showDummyMessage() {
        showToast(ToastType.SUCCESSFUL, "Not available yet")
    }

    private fun isIconifiedDefault() = !settingsUtils.searchIcon

    private fun getRemoteDataStatus() {
        WorkManager.getInstance().getStatusesForUniqueWork("Download")
                .observe(this, Observer { list ->
                    list?.let {

                        if (it.isNotEmpty() && it[0].state.isFinished && !isShown) {

                            val builder = NotificationCompat
                                    .Builder(this, packageName)
                            builder.setContentTitle("New Data")
                            builder.setContentText("Data added remotely")
                            builder.priority = NotificationCompat.PRIORITY_DEFAULT
                            builder.setSmallIcon(R.drawable.ic_019_information_button,
                                    NotificationCompat.PRIORITY_DEFAULT)

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra(Intent.EXTRA_TEXT, true)

                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            val pendingIntent = PendingIntent.getActivity(this,
                                    0, intent, 0)

                            builder.setContentIntent(pendingIntent)
                            builder.setAutoCancel(true)

                            val manager = NotificationManagerCompat.from(this)
                            manager.notify(121, builder.build())

                            //shown
                            isShown = true
                        }
                    }
                })
    }

    companion object {
        var isShown = true
    }
}
