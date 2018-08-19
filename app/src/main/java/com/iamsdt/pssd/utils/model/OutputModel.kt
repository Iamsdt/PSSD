/*
 * Developed By Shudipto Trafder
 * on 8/19/18 10:14 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.model

import com.google.gson.annotations.SerializedName
import com.iamsdt.pssd.database.WordTable

class OutputModel(@SerializedName("type") val type: String,
                  @SerializedName("date")val date: Long,
                  @SerializedName("list")val list: List<WordTable>)