/*
 * Developed By Shudipto Trafder
 * on 8/18/18 12:09 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.service

import com.google.gson.annotations.SerializedName

class Model(
        @SerializedName("word") val word: String,
        @SerializedName("des") val des: String
)