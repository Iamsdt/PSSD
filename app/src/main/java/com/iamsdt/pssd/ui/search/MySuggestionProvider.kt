/*
 * Developed By Shudipto Trafder
 *  on 8/21/18 10:20 PM
 *  Copyright (c) 2018  Shudipto Trafder.
 */

/*
 * Created by Shudipto Trafder
 * on 6/13/18 9:38 AM
 */

package com.iamsdt.pssd.ui.search

import android.content.SearchRecentSuggestionsProvider


class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {

        const val AUTHORITY = "com.iamsdt.pssd.MySuggestionProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }
}