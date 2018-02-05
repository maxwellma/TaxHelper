package com.maxwell.taxhelper.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.maxwell.taxhelper.R
import com.maxwell.taxhelper.busevent.BuchonggjjEvent
import de.greenrobot.event.EventBus

/**
 * Created by maxwellma on 18/11/2017.
 */
class GjjParamSelector(context: Context) {

    private var mPopupWindow: PopupWindow? = null
    var contentView: View? = null

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_gjj_params, null, false)
        contentView!!.setOnClickListener { _ -> dismiss() }
        contentView!!.findViewById(R.id.title).setOnClickListener {}
        var content = contentView!!.findViewById(R.id.content) as LinearLayout
        var i = 0
        while (i < 9) {
            var j = i
            var itemView = LayoutInflater.from(context).inflate(R.layout.item_text, content, false)
            (itemView.findViewById(R.id.textView) as TextView).text = i.toString() + "%"
            itemView.setOnClickListener {
                dismiss()
                EventBus.getDefault().post(BuchonggjjEvent(j))
            }
            content.addView(itemView)
            i++
        }
        mPopupWindow = PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true)
        mPopupWindow!!.animationStyle = R.style.PushInOutStyle
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.update()
    }

    fun show(view: View) {
        mPopupWindow!!.showAtLocation(view, Gravity.BOTTOM, 0, 0)
        contentView!!.postDelayed({  contentView!!.setBackgroundResource(R.color.app_alpha_black_dark) }, 300)
    }

    fun dismiss() {
        mPopupWindow!!.contentView.setBackgroundResource(android.R.color.transparent)
        contentView!!.postDelayed({ -> mPopupWindow!!.dismiss() }, 10)
    }

}