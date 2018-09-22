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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.gone
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ext.toNextActivity
import com.iamsdt.pssd.ui.DeveloperActivity
import com.iamsdt.pssd.ui.add.AddActivity
import com.iamsdt.pssd.ui.color.ColorActivity
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.details.DetailsActivity
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
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        LifecycleOwner {

    private val syncTask: SyncTask by inject()

    private val restoreData: RestoreData by inject()

    private val themeRequestCode = 121


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        syncTask.initialize(this)
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
