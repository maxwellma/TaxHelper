package com.maxwell.taxhelper

import android.os.Bundle
import com.maxwell.projectfoundation.BaseActivity
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_about_us.*

/**
 * Created by maxwellma on 04/12/2017.
 */
class AboutUsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity(R.layout.activity_about_us, R.string.aboutus)
        appName.text = getString(R.string.app_name) + " v" + packageManager.getPackageInfo(packageName, 0).versionName
    }

    override fun onResume() {
        super.onResume()
        TCAgent.onPageStart(this, "aboutUs")
    }
}