package com.maxwell.taxhelper

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxwell.taxhelper.bean.CityParams
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by maxwellma on 01/04/2018.
 */
class CityParamsProvider {

    var cityParamsList = ArrayList<CityParams>()
        private set(value) {}

    companion object {
        private var cityParamsProvider: CityParamsProvider? = null

        fun getInstance(context: Context): CityParamsProvider {
            synchronized(CityListProvider::class.java) {
                if (cityParamsProvider == null) {
                    cityParamsProvider = CityParamsProvider()
                    cityParamsProvider!!.cityParamsList.clear()
                    cityParamsProvider!!.cityParamsList.addAll(cityParamsProvider!!.readDefaultParams(context)!!)
                    Collections.sort(cityParamsProvider!!.cityParamsList, { left, right -> left.pinyin[0] - right.pinyin[0] })
                    cityParamsProvider!!.adjustDigit()
                }
                return cityParamsProvider!!
            }
        }
    }

    private constructor()

    private fun readDefaultParams(context: Context): ArrayList<CityParams>? {
        return try {
            var reader = BufferedReader(InputStreamReader(context.resources.assets.open("cityMap.json")))
            var json = ""
            var line: String? = reader.readLine()
            while (line != null) {
                json += line
                line = reader.readLine()
            }
            var cityParams = Gson().fromJson<ArrayList<CityParams>>(json!!)
            cityParams
        } catch (e: Exception) {
            Log.e("cityMap", e.message)
            null
        }
    }

    fun getCityParams(name: String): CityParams? {
        return cityParamsList.filter { it -> it.name == name.removeSuffix("市") }.getOrNull(0)
    }

    inline fun <reified T> Gson.fromJson(json: String): T {
        return this.fromJson<T>(json, object : TypeToken<T>() {}.type)!!
    }

    fun loadCityMaps() {
        OkHttpClient().newCall(Request.Builder().url("http://otbzjkm6x.bkt.clouddn.com/cityMap").build()).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                if (!body.isNullOrEmpty() && response?.code() == 200) {
                    var cityMap = Gson().fromJson<ArrayList<CityParams>>(body!!)
                    if (cityMap != null && !cityMap.isEmpty()) {
                        cityParamsProvider!!.cityParamsList.clear()
                        cityParamsProvider!!.cityParamsList.addAll(cityMap)
                        Collections.sort(cityParamsProvider!!.cityParamsList, { left, right -> left.pinyin[0] - right.pinyin[0] })
                        adjustDigit()
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
            }

        })
    }

    fun adjustDigit() {
        cityParamsList.forEach { it ->
            it.insurance.c_housing = it.insurance.c_housing / 100
            it.insurance.c_injury = it.insurance.c_injury / 100
            it.insurance.c_maternity = it.insurance.c_maternity / 100
            it.insurance.c_medical = it.insurance.c_medical / 100
            it.insurance.c_unemployed = it.insurance.c_unemployed / 100
            it.insurance.c_pension = it.insurance.c_pension / 100
            it.insurance.p_housing = it.insurance.p_housing / 100
            it.insurance.p_medical = it.insurance.p_medical / 100
            it.insurance.p_pension = it.insurance.p_pension / 100
            it.insurance.p_unemployed = it.insurance.p_unemployed / 100
        }
    }
}