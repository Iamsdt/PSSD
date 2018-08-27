/*
 * Developed By Shudipto Trafder
 *  on 8/27/18 5:20 PM
 *  Copyright (c)2018  Shudipto Trafder.
 */

package com.iamsdt.pssd.utils.model

import com.google.gson.annotations.SerializedName

class RemoteModel(@SerializedName("type") val type: String,
                  @SerializedName("date")val date: Long,
                  @SerializedName("list")val list: List<Model>)