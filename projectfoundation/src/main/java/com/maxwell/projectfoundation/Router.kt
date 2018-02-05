package com.maxwell.projectfoundation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import com.maxwell.projectfoundation.provider.RouterMapper
import org.apache.commons.lang3.StringUtils


/**
 * Created by maxwellma on 28/08/2017.
 */
class Router {

    private constructor()

    companion object {
        private var routerInstance: Router? = null

        @JvmStatic
        fun getInstance(): Router {
            if (routerInstance == null) {
                routerInstance = Router()
            }
            return routerInstance!!
        }
    }

    @Throws(Exception::class)
    fun initialize(context: Context) {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        for (activityInfo in packageInfo.activities) {
            val metaInfo = context.packageManager.getActivityInfo(ComponentName(activityInfo.packageName, activityInfo.name),
                    PackageManager.GET_META_DATA)
            if (metaInfo.metaData != null) {
                val moduleName = metaInfo.metaData.getString("moduleName")
                if (StringUtils.isEmpty(moduleName)) {
                    continue
                }
                val moduleNames = moduleName.split("\\|")
                for (moduleKey in moduleNames) {
                    if (!StringUtils.isEmpty(moduleKey)) {
                        if (RouterMapper.getInstance().isModuleExist(moduleKey)) {
                            throw Exception()
                        }
                        RouterMapper.getInstance().addModuleInfo(moduleKey, activityInfo.name)
                    }
                }
            }
        }
    }

    fun route(context: Context, url: String?) {
        route(context, url, null, null, null)
    }

    fun route(context: Context, moduleName: String, params: Map<String, String>?) {
        var uri = Uri.parse("mcTax://" + moduleName)
        if (params != null && !params.isEmpty()) {
            var builder = uri.buildUpon()
            for((key, value) in params) {
                builder.appendQueryParameter(key, value)
            }
            uri = builder.build()
        }
        route(context, uri.toString(), params, null, null)
    }

    private fun route(context: Context, url: String?, params: Map<String, String>?, bundle: Bundle?, flags: List<Int>?) {
        if(url.isNullOrEmpty()) {
            return
        }
        var uri : Uri = Uri.parse( url)
        var target = RouterMapper.getInstance().getModuleInfo(uri.host)
        if (params != null && !params.isEmpty()) {
            var builder = uri.buildUpon()
            for((key, value) in params) {
                builder.appendQueryParameter(key, value)
            }
            uri = builder.build()
        }
        if (StringUtils.isNotEmpty(target)) {
            var intent = Intent()
            intent.setClassName(context, target)
            intent.data = uri
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            if (flags != null && !flags.isEmpty()) {
                for (flag in flags) {
                    intent.addFlags(flag)
                }
            }
            context.startActivity(intent)
        }
    }
}