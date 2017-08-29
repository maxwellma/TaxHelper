package com.maxwell.projectfoundation

import android.app.Activity
import android.os.Bundle
import com.maxwell.mclib.util.ApkTool

/**
 * Created by maxwellma on 29/08/2017.
 */
open class BaseActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!ApkTool.getApkSignature(this).equals("2CD78672996A27557FA149ED4ADD0C8C", true)) {
            finish()
        }
    }
}