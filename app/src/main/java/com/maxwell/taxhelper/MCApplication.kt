package com.maxwell.taxhelper

import android.app.Application
import com.maxwell.mclib.Apk.ApkTool
import com.maxwell.projectfoundation.Router
import com.tendcloud.tenddata.TCAgent

/**
 * Created by maxwellma on 15/08/2017.
 */
class MCApplication : Application() {

    val APP_ID: String = "D052B0CF68F546C9A45CF85A227952C1"

    override fun onCreate() {
        super.onCreate()
        Router.getInstance().initialize(this)
        TCAgent.init(this@MCApplication, APP_ID, ApkTool.getChannel(this))
        TCAgent.setReportUncaughtExceptions(true)
    }


}