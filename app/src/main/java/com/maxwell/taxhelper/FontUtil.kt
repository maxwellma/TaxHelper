package com.maxwell.taxhelper

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView

/**
 * Created by maxwellma on 28/07/2017.
 */
class FontUtil {
    companion object {
        var font = "fonts/DINNextRoundedLTPro-Regular.ttf"

        @JvmStatic
        fun setNumberFont(context: Context, textView: TextView) {
            var typeface = Typeface.createFromAsset(context.assets, font)
            textView.typeface = typeface
        }
    }
}