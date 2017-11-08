package com.maxwell.mclib.Apk

import android.content.Context
import android.content.pm.PackageManager
import com.maxwell.mclib.encode.MD5
import org.apache.commons.lang3.StringUtils
import java.lang.Exception
import java.util.zip.ZipFile


/**
 * Created by maxwellma on 29/08/2017.
 */
object ApkTool {

    private var channel: String? = null

    fun getChannel(context: Context): String? {
        if (StringUtils.isNotEmpty(channel)) {
            return channel
        } else {
            val start_flag = "META-INF/channel_"
            val appinfo = context.applicationInfo
            val sourceDir = appinfo.sourceDir
            var zipfile: ZipFile? = null
            try {
                zipfile = ZipFile(sourceDir)
                val entries = zipfile.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    val entryName = entry.name
                    if (StringUtils.contains(entryName, start_flag)) {
                        channel = StringUtils.replace(entryName, start_flag, "")
                        break
                    }
                }
            } catch (e: Exception) {
            } finally {
                if (zipfile != null) {
                    try {
                        zipfile.close()
                    } catch (e: Exception) {
                    }

                }
            }

            if (StringUtils.isEmpty(channel)) {
                channel = "officialSite"
            }
            return channel
        }
    }

    fun getApkSignature(context: Context): String {
        var signature = ""
        try {
            val sigs = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES).signatures
            if (sigs != null && sigs.size > 0) {
                signature = MD5.getMd5LowerCase(sigs[0].toByteArray())!!
            }

        } catch (e: Exception) {

        }

        return signature
    }
}