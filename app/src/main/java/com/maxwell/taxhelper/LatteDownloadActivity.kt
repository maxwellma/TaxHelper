package com.maxwell.taxhelper

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.Router
import com.maxwell.projectfoundation.provider.ActivityStackProvider
import com.maxwell.projectfoundation.provider.ConfigProvider
import com.maxwell.projectfoundation.util.ToastUtil
import kotlinx.android.synthetic.main.activity_latte_download.*


/**
 * Created by maxwellma on 25/01/2018.
 */

class LatteDownloadActivity : BaseActivity() {

    var title: String? = null
    var url: String? = null
    var downloadTitle: String? = null
    var downloadDesc: String? = null
    var showDownload: Boolean = false
    var downloadUrl: String? = null
    var showShare = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.data?.getQueryParameter("url")
        title = intent.data?.getQueryParameter("title")
        showDownload = intent.data?.getBooleanQueryParameter("showDownload", false)!!
        downloadUrl = intent.data?.getQueryParameter("downloadUrl")
        downloadTitle = intent.data?.getQueryParameter("downloadTitle")
        downloadDesc = intent.data?.getQueryParameter("downloadDesc")
        showShare = intent.data!!.getBooleanQueryParameter("share", false)

        initActivity(R.layout.activity_latte_download, title!!)
        initViews()
    }

    private fun initViews() {
        if (showShare && ConfigProvider.getInstance().shareConfig != null && !ConfigProvider.getInstance().shareConfig?.title.isNullOrEmpty()) {
            findViewById(R.id.action).visibility = View.VISIBLE
            findViewById(R.id.action).setOnClickListener { _ ->
                var textIntent = Intent(Intent.ACTION_SEND)
                textIntent.type = "text/plain"
                textIntent.putExtra(Intent.EXTRA_TEXT, ConfigProvider.getInstance().shareConfig?.title)
                startActivity(Intent.createChooser(textIntent, "分享"))
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                try {
                    if (url != null && (url.matches(Regex("^http://answer\\.sm\\.cn.*$")) || url.matches(Regex("^https://answer\\.sm\\.cn.*$")))) {
                        webView.loadUrl("javascript:(document.getElementsByClassName(\"uc\"))[0].parentNode.removeChild((document.getElementsByClassName(\"uc\"))[0])")
                    }
                } catch (e: Exception) {
                }
            }
        })
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                val calculatedProgress = newProgress / 10 * 9 + 10
                loadingProgressBar.progress = calculatedProgress
                if (calculatedProgress > 30) {
                    if (showDownload) {
                        download.visibility = View.VISIBLE
                    }
                    if (newProgress === 100) {
                        webView.requestFocus(View.FOCUS_DOWN)
                        loadingProgressBar.postDelayed({ loadingProgressBar.visibility = View.GONE }, 300)
                    }
                }
                if (calculatedProgress != 100) {
                    loadingProgressBar.visibility = View.VISIBLE
                }

            }
        })
        if (url.isNullOrEmpty()) {
            webView.loadUrl("https://www.lattebank.com")
        } else {
            webView.loadUrl(url)
        }
        download.text = downloadDesc
        var appName = if (url!!.contains("lattebank")) {
            "LatteAdvisor"
        } else {
            "Huanbei"
        } + System.currentTimeMillis().toString() + ".apk"
        download.setOnClickListener { _ ->
            var downloadReq = DownloadManager.Request(Uri.parse(downloadUrl))
            downloadReq.setAllowedOverRoaming(true)
            if (!downloadTitle.isNullOrEmpty()) {
                downloadReq.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                downloadReq.setTitle(downloadTitle)
                downloadReq.setDescription(downloadDesc)
            } else {
                downloadReq.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            }
            downloadReq.setMimeType("application/vnd.android.package-archiv")
            downloadReq.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, appName)
            var downloadManager = (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
            var downloadId = downloadManager.enqueue(downloadReq)
            ToastUtil.show(this@LatteDownloadActivity, "正在下载中，请在手机顶部的通知栏查看下载进度", Toast.LENGTH_LONG)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (ActivityStackProvider.getInstance().isTaskRoot()) {
            Router.getInstance().route(this@LatteDownloadActivity, "main", null)
        }
    }
}
