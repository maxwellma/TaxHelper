package com.maxwell.taxhelper

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.maxwell.mclib.view.QuickIndexBar
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.manager.FullLoadingManager
import com.maxwell.taxhelper.adapter.QuickIndexAdapter
import com.maxwell.taxhelper.busevent.CityLoadEvent
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import de.greenrobot.event.ThreadMode
import kotlinx.android.synthetic.main.acitivity_city_list.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by maxwellma on 04/11/2017.
 */
class CityListActivity : BaseActivity() {

    private var fullLoadingManager = FullLoadingManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity(R.layout.acitivity_city_list, R.string.city_list)
        fullLoadingManager.showLoadingLayout()
        EventBus.getDefault().register(this)
        Thread({
            CityParamsProvider.getInstance(this)
            EventBus.getDefault().post(CityLoadEvent())
        }).start()
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    fun onloadEvent(cityLoadEvent: CityLoadEvent) {
        initView()
    }

    private fun initView() {
        var cityList = CityParamsProvider.getInstance(this).cityParamsList
        fullLoadingManager.removeLoadLayout()
        (this.recyclerView as RecyclerView).adapter = QuickIndexAdapter(this, cityList)
        (this.recyclerView as RecyclerView).layoutManager = LinearLayoutManager(this@CityListActivity)
        (this.quickIndexBar as QuickIndexBar).setOnLetterUpdateListener(object : QuickIndexBar.OnLetterUpdateListener {
            override fun onLetterUpdate(letter: String) {
                (this@CityListActivity.tip as TextView).text = letter
                (this@CityListActivity.tip as TextView).visibility = View.VISIBLE

                for (i in cityList.indices) {
                    if (StringUtils.equalsIgnoreCase(cityList[i].pinyin[0].toString(), letter)) {
                        ((this@CityListActivity.recyclerView as RecyclerView).layoutManager as LinearLayoutManager).scrollToPositionWithOffset(i, 0)
                        break
                    }
                }
            }

            override fun onHiddenLetter() {
                (this@CityListActivity.tip as TextView).visibility = View.GONE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}