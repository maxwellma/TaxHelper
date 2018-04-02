package com.maxwell.taxhelper.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by maxwellma on 01/04/2018.
 */
data class CityParams(@SerializedName("id") var id: String,
                      @SerializedName("name_hz") var name: String,
                      @SerializedName("name_py") var pinyin: String,
                      @SerializedName("insurance") var insurance: Insurance)