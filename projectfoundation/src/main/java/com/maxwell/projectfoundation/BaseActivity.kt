package com.maxwell.projectfoundation

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.maxwell.mclib.Apk.ApkTool
import com.maxwell.projectfoundation.provider.ActivityStackProvider

/**
 * Created by maxwellma on 29/08/2017.
 */
open class BaseActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!ApkTool.getApkSignature(this).equals("2CD78672996A27557FA149ED4ADD0C8C", true)) {
            finish()
        }
        ActivityStackProvider.getInstance().put(this@BaseActivity)
    }

    fun initActivity(contentLayout: Int, title: Int) {
        initActivity(contentLayout, getString(title))
    }

    fun initActivity(contentLayout: Int, title: String) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE)
        setContentView(contentLayout)
        window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_common)

        findViewById(R.id.title_back).setOnClickListener(onBackClickedListener)
        (findViewById(R.id.title_text) as TextView).text = title
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackProvider.getInstance().remove(this@BaseActivity)
    }

    var onBackClickedListener = View.OnClickListener { this@BaseActivity.onBackPressed() }
}