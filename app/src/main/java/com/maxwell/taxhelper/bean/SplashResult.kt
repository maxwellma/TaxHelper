package com.maxwell.taxhelper.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by maxwellma on 29/01/2018.
 */
data class SplashResult(@SerializedName("imgUrl") var imageUrl: String? = "", @SerializedName("jumpUrl") var jumpUrl: String? = "", @SerializedName("slogan") var slogan: String? = "", @SerializedName("app") var app: String? = null) : Serializable {
    companion object {
        val LATTE = "latte"
        val HUANBEI = "huanbei"
        val TAXHELPER = "taxHelper"
    }
}