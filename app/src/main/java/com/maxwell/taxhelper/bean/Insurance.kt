package com.maxwell.taxhelper.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by maxwellma on 02/04/2018.
 */
data class Insurance(@SerializedName("p_pension") var p_pension: Double,
                     @SerializedName("p_medical") var p_medical: Double,
                     @SerializedName("p_unemployed") var p_unemployed: Double,
                     @SerializedName("p_housing") var p_housing: Double,
                     @SerializedName("p_housing_extra") var p_housing_extra: Double = 0.0,
                     @SerializedName("c_pension") var c_pension: Double,
                     @SerializedName("c_medical") var c_medical: Double,
                     @SerializedName("c_unemployed") var c_unemployed: Double,
                     @SerializedName("c_housing") var c_housing: Double,
                     @SerializedName("c_housing_extra") var c_housing_extra: Double = 0.0,
                     @SerializedName("c_injury") var c_injury: Double,
                     @SerializedName("c_maternity") var c_maternity: Double,
                     @SerializedName("maxSocial") var maxSocial: Double,
                     @SerializedName("minSocial") var minSocial: Double,
                     @SerializedName("maxfunds") var maxFunds: Double,
                     @SerializedName("minfunds") var minFunds: Double
)