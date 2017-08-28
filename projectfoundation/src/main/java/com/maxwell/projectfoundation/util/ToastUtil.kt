package com.maxwell.projectfoundation.util

import android.content.Context
import android.widget.Toast

/**
 * Created by maxwellma on 28/08/2017.
 */
class ToastUtil {

    companion object {
        @JvmStatic
        var toast: Toast? = null

        @JvmStatic
        fun show(context: Context, message: String, length: Int) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, message, length)
            toast!!.show()
        }

        @JvmStatic
        fun show(context: Context, messageRes: Int, length: Int) {
            show(context, context.resources.getString(messageRes), length)
        }
    }
}