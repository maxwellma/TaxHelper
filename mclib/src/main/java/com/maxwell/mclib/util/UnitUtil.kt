package com.maxwell.mclib.util

import android.content.Context

/**
 * Created by maxwellma on 04/11/2017.
 */
object UnitUtil {

    fun dpToPix(context: Context, value : Float) : Int{
        var scale = context.resources.displayMetrics.density
        return  (value * scale + 0.5f).toInt()
    }

    fun pixToDp(context: Context, value : Float) : Int {
        var scale = context.resources.displayMetrics.density
        return  (value / scale + 0.5f).toInt()
    }
}