package com.maxwell.taxhelper

import android.os.Bundle
import android.widget.Toast
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.Router
import com.maxwell.projectfoundation.util.ToastUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
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
        this.annual_entry.setOnClickListener { _ -> Router.getInstance().route(this, "annualBonus") }
        this.month_entry.setOnClickListener { _ -> ToastUtil.show(this@MainActivity, "该功能正在开发中，即将开放~", Toast.LENGTH_SHORT) }
    }

}
