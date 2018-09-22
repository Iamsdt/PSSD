/*
 * Developed By Shudipto Trafder
 *  on 9/22/18 9:25 AM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.iamsdt.pssd.R
import com.iamsdt.pssd.ext.ToastType
import com.iamsdt.pssd.ext.gone
import com.iamsdt.pssd.ext.showToast
import com.iamsdt.pssd.ui.details.DetailsActivity
import com.iamsdt.pssd.ui.search.MySuggestionProvider
import com.iamsdt.pssd.utils.Constants
import com.iamsdt.pssd.utils.RestoreData
import com.iamsdt.pssd.utils.SettingsUtils
import com.iamsdt.pssd.utils.sync.SyncTask
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Created by Shudipto Trafder on 9/22/2018.
 * at 9:25 AM
 */
class MainFragment : Fragment() {

    private val viewModel: MainVM by viewModel()

    private lateinit var adapter: MainAdapter

    private val settingsUtils: SettingsUtils by inject()

    private var suggestions: SearchRecentSuggestions? = null

    lateinit var searchView: androidx.appcompat.widget.SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        suggestions = SearchRecentSuggestions(context,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)

        val intent = activity?.intent

        handleSearch(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.mainRcv.layoutManager = LinearLayoutManager(context)
        adapter = MainAdapter(context!!)
        view.mainRcv.adapter = adapter

        val deco = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        deco.setDrawable(context!!.getDrawable(R.drawable.dercoration))

        // Complete: 8/22/18 add item decoration
        view.mainRcv.addItemDecoration(deco)

        viewModel.liveData.observe(activity!!, Observer {
            adapter.submitList(it)
        })

        viewModel.event.observe(this, Observer { model ->
            model?.let {
                if (it.title == Constants.SEARCH) {
                    if (it.status) {
                        Timber.i("Status is true")
                        val intent = Intent(context, DetailsActivity::class.java)
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

        return view
    }

    private fun setRecentQuery(query: String) {
        suggestions?.saveRecentQuery(query, null)
    }

    private fun handleSearch(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            // complete: 6/14/2018 search
            viewModel.submit(query)

            //Search
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Search Data")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "User search query")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Search $query")
            FirebaseAnalytics.getInstance(context!!)
                    .logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        searchView = menu.findItem(R.id.search)?.actionView as SearchView
        //search
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

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

                val ana = FirebaseAnalytics.getInstance(context!!)
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

                //fab.gone()
                return true
            }

        })

        searchView.setOnClickListener {


        }
    }

    private fun isIconifiedDefault() = !settingsUtils.searchIcon

}