package com.maxwell.projectfoundation.provider

import com.google.gson.Gson
import com.maxwell.projectfoundation.bean.SalaryConfig
import okhttp3.*
import java.io.IOException

/**
 * Created by maxwellma on 30/01/2018.
 */
class ConfigProvider {

    private constructor()

    var salaryConfig: SalaryConfig? = null
    var bonusConfig: SalaryConfig? = null
    var mainButton: SalaryConfig? = null
    var shareConfig: SalaryConfig? = null

    companion object {
        var configProvider: ConfigProvider? = null
        fun getInstance(): ConfigProvider {
            if (configProvider == null) {
                configProvider = ConfigProvider()
            }
            return configProvider!!
        }
    }

    fun initSalaryConf() {
        OkHttpClient().newCall(Request.Builder().url("http://otbzjkm6x.bkt.clouddn.com/salary").build()).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response?.code() == 200) {
                    var body = response?.body()?.string()
                    if (response != null && !body.isNullOrEmpty()) {
                        salaryConfig = Gson().fromJson(body, SalaryConfig::class.java)
                    }
                }
            }

        })
    }

    fun initBonusConf() {
        OkHttpClient().newCall(Request.Builder().url("http://otbzjkm6x.bkt.clouddn.com/bonus").build()).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                if (response != null && !body.isNullOrEmpty()) {
                    bonusConfig = Gson().fromJson(body, SalaryConfig::class.java)
                }
            }

        })
    }

    fun initForthButton() {
        OkHttpClient().newCall(Request.Builder().url("http://otbzjkm6x.bkt.clouddn.com/mainButton").build()).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                if (response != null && !body.isNullOrEmpty()) {
                    mainButton = Gson().fromJson(body, SalaryConfig::class.java)
                }
            }

        })
    }

    fun initShareConf() {
        OkHttpClient().newCall(Request.Builder().url("http://otbzjkm6x.bkt.clouddn.com/shareConfig").build()).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {

            }

            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                if (response != null && !body.isNullOrEmpty()) {
                    shareConfig = Gson().fromJson(body, SalaryConfig::class.java)
                }
            }

        })
    }
}