package com.maxwell.taxhelper

import android.app.Application
import com.tendcloud.tenddata.TCAgent

/**
 * Created by maxwellma on 15/08/2017.
 */
class MCApplication : Application() {

    val APP_ID: String = "D052B0CF68F546C9A45CF85A227952C1"

    override fun onCreate() {
        super.onCreate()
        TCAgent.init(this@MCApplication, APP_ID, "officialSite")
        TCAgent.setReportUncaughtExceptions(true)
    }


}