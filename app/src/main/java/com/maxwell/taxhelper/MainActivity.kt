package com.maxwell.taxhelper

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.maxwell.mclib.Location.LocationHelper
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.Router
import com.maxwell.projectfoundation.provider.ConfigProvider
import com.maxwell.projectfoundation.util.ToastUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        CityListProvider.getInstance()
        ConfigProvider.getInstance().initSalaryConf()
        ConfigProvider.getInstance().initBonusConf()
        ConfigProvider.getInstance().initShareConf()
    }


    override fun onResume() {
        super.onResume()
        TCAgent.onPageStart(this, "main")
    }

    override fun onDestroy() {
        super.onDestroy()
        TCAgent.onPageEnd(this, "main")
    }

    private fun initViews() {
        this.about_us.setOnClickListener { _ -> Router.getInstance().route(this, "aboutUs", null) }
        this.annual_entry.setOnClickListener { _ -> Router.getInstance().route(this, "annualBonus", null) }
        this.month_entry.setOnClickListener { _ -> Router.getInstance().route(this, "salary", null) }
        this.house_entry.setOnClickListener { _ -> ToastUtil.show(this@MainActivity, "即将开放，敬请期待~", Toast.LENGTH_SHORT) }
        if (!ConfigProvider.getInstance().mainButton?.title.isNullOrEmpty()) {
            (this.forthEntry as TextView).text = ConfigProvider.getInstance().mainButton?.title
            this.forthEntry.setOnClickListener { _ -> Router.getInstance().route(this@MainActivity, ConfigProvider.getInstance().mainButton?.jumpUrl) }
            this.forthEntry.setBackgroundResource(R.drawable.entry_orange_button)
            (this.forthEntry as TextView).textSize = 14f
        }

        LocationHelper.startLocation(this)
    }
}
