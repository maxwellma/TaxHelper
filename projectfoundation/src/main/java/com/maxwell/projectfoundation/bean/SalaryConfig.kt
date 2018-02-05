package com.maxwell.projectfoundation.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by maxwellma on 30/01/2018.
 */
data class SalaryConfig(@SerializedName("title") var title: String?, @SerializedName("actionText") var actionText: String?, @SerializedName("jumpUrl") var jumpUrl: String?)