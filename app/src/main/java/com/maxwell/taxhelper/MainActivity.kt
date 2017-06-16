package com.maxwell.taxhelper

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import taxhelper.maxwell.com.taxhelper.R
import java.io.IOException


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        this.salaryInput.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> calculateAward(salaryInput)
            }
            true
        }
    }

    private fun calculateAward(mEditText: EditText) {
        hideKeyBoard(mEditText)
        var formBody = FormBody.Builder().add("cityname", "上海").add("yearAward", mEditText.text?.toString()).build()
        var request = Request.Builder().url("https://www.rong360.com/calculator/yearAward.html").post(formBody).build()

        OkHttpClient().newCall(request).enqueue(callback)

        var map = HashMap<String, Any>()
        map.put("name", "maxwell")
        map.put("age", 1)
        map.put("origin", Student("maxwell", 1))
        map.put("json", JSONObject("{'name':'max', 'age':1}"))
        salaryInput.setText(Uri.decode("__cjjTitle=%20"))

    }

    private fun hideKeyBoard(mEditText: EditText) {
        (this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    var callback = object : Callback {
        override fun onFailure(call: Call?, e: IOException?) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, e?.message, Toast.LENGTH_LONG).show()
            }
        }

        override fun onResponse(call: Call?, response: Response?) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, response?.body()?.string(), Toast.LENGTH_LONG).show()
            }
        }
    }

    class Student (name :String, age : Int) {
    }
}
