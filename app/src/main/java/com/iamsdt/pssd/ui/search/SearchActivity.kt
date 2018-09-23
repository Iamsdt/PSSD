/*
 * Developed By Shudipto Trafder
 * on 8/17/18 9:56 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.color.ThemeUtils
import com.iamsdt.pssd.ui.details.DetailsActivity
import com.iamsdt.pssd.ui.main.MainAdapter
import com.iamsdt.pssd.utils.Constants
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchVM by viewModel()

    private var suggestions: SearchRecentSuggestions? = null


    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.initialize(this)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        searchRcv.layoutManager = LinearLayoutManager(this)
        //val adapter = MainAdapter(this)
        //searchRcv.adapter = adapter

        // performance issue
        // complete: 8/22/18 fix latter make single live data

        viewModel.liveData.observe(this, Observer {
            Timber.i("size: ${it.size}")
            //adapter.submitList(it)
        })

        //complete: I am not happy with this solution
        viewModel.event.observe(this, Observer {
            it?.let {
                if (it.title == Constants.SEARCH) {
                    if (it.status) {
                        Timber.i("Status is true")
                        val intent = Intent(this, DetailsActivity::class.java)
                        intent.putExtra(Intent.EXTRA_TEXT, it.message.toInt())
                        startActivity(intent)
                    } else {
                        Timber.i(it.message)
                        showToast(ToastType.ERROR, it.message)
                    }
                }
            }
        })

        //set suggestion option
        suggestions = SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)
        handleSearch(intent)
    }

    private fun setRecentQuery(query: String) {
        suggestions?.saveRecentQuery(query, null)

    }

    override fun onNewIntent(intent: Intent) {
        handleSearch(intent)
        Timber.i("Call")
    }

    private fun handleSearch(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            // complete: 6/14/2018 search
            viewModel.submit(query)
            setRecentQuery(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        //search
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)
        searchView.isQueryRefinementEnabled = true
        searchView.requestFocus()

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
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.i("call")
                newText?.let {
                    Timber.i("new text is $newText")
                    viewModel.requestSearch(it)
                }
                return true
            }

        })

        searchView.setOnCloseListener {
            showToast(ToastType.INFO, "Search view closed")

            false
        }

        searchView.setOnFocusChangeListener { v, hasFocus ->
            if (v.isFocused) {
                showToast(ToastType.INFO, "Search view closed")
            }

            if (!hasFocus){
                showToast(ToastType.INFO, "Search view closed")
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        else if (searchView.isIconified) {
            searchView.close()
        }

        return super.onOptionsItemSelected(item)
    }

    fun SearchView.show() {
        this.isIconified = false
    }

    fun SearchView.close() {
        this.isIconified = true
        searchView.clearFocus()
    }
}
