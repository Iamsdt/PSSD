/*
 * Developed By Shudipto Trafder
 * on 8/19/18 10:14 AM
 * Copyright (c) 2018 Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.model

import com.google.gson.annotations.SerializedName

class JsonModel(
        @SerializedName("volume") val volume: Int,
        @SerializedName("collection") val collection: List<Model>
)

