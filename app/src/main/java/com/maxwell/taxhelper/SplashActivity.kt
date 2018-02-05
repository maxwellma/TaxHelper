package com.maxwell.taxhelper

import android.os.Bundle
import android.os.Handler
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import com.github.mikephil.charting.utils.Utils
import com.google.gson.Gson
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.Router
import com.maxwell.projectfoundation.provider.ConfigProvider
import com.maxwell.taxhelper.bean.SplashResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.acitivity_splash.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

/**
 * Created by maxwellma on 25/01/2018.
 */
class SplashActivity : BaseActivity() {

    var hasImageResult = false
    var redirected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_splash)
        loadSplashImg()
        ConfigProvider.getInstance().initForthButton()
        Handler().postDelayed({
            if (!hasImageResult) {
                Router.getInstance().route(this@SplashActivity, "main", null)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }, 3000)
    }

    private fun loadSplashImg() {
        OkHttpClient().newCall(Request.Builder().url("http://otbzjkm6x.bkt.clouddn.com/splash").build()).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                var body = response?.body()?.string()
                if (!body.isNullOrEmpty()) {
                    var splashResult = Gson().fromJson(body, SplashResult::class.java)
                    if (splashResult == null || splashResult.imageUrl.isNullOrEmpty()) {
                        return
                    }
                    hasImageResult = true
                    splash_img.post {
                        Picasso.with(this@SplashActivity).load(JSONObject(body).getString("imgUrl")).into(splash_img, object : com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                if (!splashResult.jumpUrl.isNullOrEmpty()) {
                                    splash_img.setOnClickListener { _ ->
                                        redirected = true
                                        Router.getInstance().route(this@SplashActivity, JSONObject(body).getString("jumpUrl"))
                                        finish()
                                    }
                                }
                                splash_img.postDelayed({
                                    if (!redirected) {
                                        Router.getInstance().route(this@SplashActivity, "main", null)
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        finish()
                                    }
                                }, 4000)
                                animateImg(splashResult)
                            }

                            override fun onError() {
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
            }

        })
    }

    override fun onBackPressed() {}

    fun animateImg(splashResult: SplashResult) {
        var scaleAnimation = ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 2500
        scaleAnimation.fillAfter = true
        scaleAnimation.startOffset = 500
        splash_img.startAnimation(scaleAnimation)

        if (splashResult != null) {
            when (splashResult.app) {
                SplashResult.LATTE -> provider_logo.setBackgroundResource(R.drawable.ic_latte)
                SplashResult.HUANBEI -> provider_logo.setBackgroundResource(R.drawable.ic_huanbei)
                else -> provider_logo.setBackgroundResource(R.mipmap.ic_launcher)
            }
            when (splashResult.slogan) {
                null, "" -> provider_slogan.text = getString(R.string.slogan)
                else -> provider_slogan.text = splashResult.slogan
            }
        }
        var translateAnimation = TranslateAnimation(0f, 0f, Utils.convertDpToPixel(360f), 0f)
        translateAnimation.duration = 800
        translateAnimation.fillAfter = true
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
            }

            override fun onAnimationStart(p0: Animation?) {
                provider_frame.visibility = VISIBLE
            }

        })
        translateAnimation.startOffset = 2000
        provider_frame.startAnimation(translateAnimation)
    }
}