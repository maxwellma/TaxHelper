package com.maxwell.taxhelper

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.maxwell.projectfoundation.util.ToastUtil


/**
 * Created by maxwellma on 29/01/2018.
 */
class DownloadCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val completeDownloadId = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        var query = DownloadManager.Query()
        var cursor = (context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).query(query.setFilterById(completeDownloadId))
        var path = ""
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
        }
        if (cursor != null) {
            cursor.close()
        }
        if (completeDownloadId > -1) {
            var intent = Intent(Intent.ACTION_VIEW)
            var appDesc = if (!path.isNullOrEmpty() && path.contains("LatteAdvisor")) {
                "安装注册后可领取投资红包"
            } else {
                "安装注册后让信用卡满血复活"
            }
            intent.setDataAndType(Uri.parse(path), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            ToastUtil.show(context, appDesc, Toast.LENGTH_LONG)
        }
    }
}