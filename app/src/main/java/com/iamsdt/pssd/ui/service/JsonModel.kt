/*
 * Developed By Shudipto Trafder
 * on 8/17/18 3:25 PM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.ui.service

import com.google.gson.annotations.SerializedName

class JsonModel(
        @SerializedName("volume") val volume: Int,
        @SerializedName("collection") val collection: List<Model>
)

class Model(
        @SerializedName("word") val word: String,
        @SerializedName("des") val des: String
)