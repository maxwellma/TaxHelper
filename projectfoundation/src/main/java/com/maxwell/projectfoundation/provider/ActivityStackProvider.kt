package com.maxwell.projectfoundation.provider

import com.maxwell.projectfoundation.BaseActivity

/**
 * Created by maxwellma on 25/01/2018.
 */
class ActivityStackProvider {

    private var activityList: ArrayList<BaseActivity> = ArrayList()

    private constructor()

    companion object {
        private var activityStackProvider: ActivityStackProvider? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): ActivityStackProvider {
            if (activityStackProvider == null) {
                activityStackProvider = ActivityStackProvider()
            }
            return activityStackProvider!!
        }
    }

    fun put(activity: BaseActivity) {
        activityList.add(activity)
    }

    fun remove(activity: BaseActivity) {
        activityList.remove(activity)
    }

    fun isTaskRoot(): Boolean {
        return activityList.size <= 1
    }
}