package com.maxwell.projectfoundation.manager

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.maxwell.projectfoundation.R




/**
 * Created by maxwellma on 01/04/2018.
 */
class FullLoadingManager {

    var mContext: Activity? = null

    constructor(context: Activity) {
        this.mContext = context
    }

    private val rootView: View? = null

    fun showLoadingLayout() {
        if (mContext == null) {
            return
        }
        val rootContent = rootView ?: mContext!!.window.findViewById(Window.ID_ANDROID_CONTENT)
        var mLinearLayout: LinearLayout? = rootContent.findViewById(R.id.cjj_full_loading_layout) as LinearLayout?
        if (mLinearLayout == null) {
            mLinearLayout = LinearLayout(mContext)
            mLinearLayout.setBackgroundColor(mContext!!.resources.getColor(R.color.b4))
            mLinearLayout.id = R.id.cjj_full_loading_layout
            val view = LayoutInflater.from(mContext).inflate(R.layout.layout_full_loading, mLinearLayout, false)
            mLinearLayout.addView(view)
            if (rootContent is FrameLayout) {
                val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                mLinearLayout.layoutParams = layoutParams
                rootContent.addView(mLinearLayout)
            }
        }

        mLinearLayout.findViewById(R.id.load_fail_tip_text).visibility = View.INVISIBLE
        mLinearLayout.findViewById(R.id.loading_image).visibility = View.VISIBLE
    }

    fun showLoadFailLayout() {
        if (mContext == null) {
            return
        }
        val rootContent = rootView ?: mContext!!.window.findViewById(Window.ID_ANDROID_CONTENT)
        var mLinearLayout: LinearLayout? = rootContent.findViewById(R.id.cjj_full_loading_layout) as LinearLayout?
        if (mLinearLayout == null) {
            mLinearLayout = LinearLayout(mContext)
            mLinearLayout.setBackgroundColor(mContext!!.resources.getColor(R.color.b4))
            mLinearLayout.id = R.id.cjj_full_loading_layout
            val view = LayoutInflater.from(mContext).inflate(R.layout.layout_full_loading, mLinearLayout, false)
            mLinearLayout.addView(view)
            if (rootContent is FrameLayout) {
                val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                mLinearLayout.layoutParams = layoutParams
                rootContent.addView(mLinearLayout)
            }
        }

        mLinearLayout.findViewById(R.id.load_fail_tip_text).visibility = View.VISIBLE
        mLinearLayout.findViewById(R.id.loading_image).visibility = View.INVISIBLE
    }

    fun removeLoadLayout() {
        if (mContext != null) {
            val rootContent = rootView ?: mContext!!.window.findViewById(Window.ID_ANDROID_CONTENT)
            val mFullLoadingView = rootContent.findViewById(R.id.cjj_full_loading_layout)
            if (mFullLoadingView != null && rootContent is FrameLayout) {
                rootContent.removeView(mFullLoadingView)
            }
        }
    }
}