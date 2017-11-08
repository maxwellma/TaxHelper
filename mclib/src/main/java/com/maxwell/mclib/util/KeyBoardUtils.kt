package com.maxwell.mclib.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by maxwellma on 04/11/2017.
 */
object KeyBoardUtils {

    fun hideKeyboard(context: Context, editText: EditText) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}