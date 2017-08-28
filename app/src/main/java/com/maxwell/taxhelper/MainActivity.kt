package com.maxwell.taxhelper

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import com.maxwell.jazzyviewpager.JazzyViewPager
import com.maxwell.projectfoundation.util.FontUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        TCAgent.onPageStart(this, "main")
    }

    override fun onDestroy() {
        super.onDestroy()
        TCAgent.onPageEnd(this, "main")
    }

    private fun initViews() {
        (this.jazzyPager as JazzyViewPager).adapter = MainAdapter(this@MainActivity)
        (this.jazzyPager as JazzyViewPager).pageMargin = 0
        (this.jazzyPager as JazzyViewPager).setTransitionEffect(JazzyViewPager.TransitionEffect.CubeOut)
        (this.jazzyPager as JazzyViewPager).setPagingEnabled(false)
        (this.jazzyPager as JazzyViewPager).setScrollDuration(400)

    }

    inner class MainAdapter : PagerAdapter {

        var context: Context
        var level: Int = 1
        var bonusResult: Float = 0.0f
        var desp: String? = null
        var resultView: View? = null

        constructor(context: Context) {
            this.context = context
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            when (position) {
                0 -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.layout_font_face, container, false)
                    var salaryInput = view.findViewById(R.id.salaryInput) as EditText
                    FontUtil.setNumberFont(context, salaryInput)
                    salaryInput.setOnEditorActionListener { _, actionId, _ ->
                        when (actionId) {
                            EditorInfo.IME_ACTION_DONE -> calculateAward(salaryInput)
                        }
                        true
                    }
                    view.findViewById(R.id.submit).setOnClickListener { _ ->
                        if (!TextUtils.isEmpty(salaryInput.text.toString())) {
                            hideKeyBoard(salaryInput)
                            salaryInput.clearFocus()
                            calculateAward(salaryInput)
                            jazzyPager.postDelayed({ ->
                                jazzyPager.setCurrentItem(1, true)
                                salaryInput.setText("")
                            }, 500)
                        }
                    }
                    container?.addView(view)
                    (this@MainActivity.jazzyPager as JazzyViewPager).setObjectForPosition(view, position)
                    return view
                }
                else -> {
                    resultView = LayoutInflater.from(context).inflate(R.layout.layout_tax_result, container, false)
                    (resultView?.findViewById(R.id.ratingBar) as RatingBar).rating = level.toFloat()
                    (resultView?.findViewById(R.id.value) as TextView).setText("%.2f".format(bonusResult))
                    FontUtil.setNumberFont(context, resultView?.findViewById(R.id.value) as TextView)
                    container?.addView(resultView)
                    (resultView?.findViewById(R.id.back))?.setOnClickListener { _ ->
                        (this@MainActivity.jazzyPager as JazzyViewPager).setCurrentItem(0, true)
                    }
                    (this@MainActivity.jazzyPager as JazzyViewPager).setObjectForPosition(resultView, position)
                    return resultView!!
                }
            }
        }

        private fun calculateAward(mEditText: EditText) {
            hideKeyBoard(mEditText)
            var amount = mEditText.text.toString().toFloat()
            var monthlyAmount = (mEditText.text.toString().toFloat()) / 12
            when (mEditText.text.toString().toFloat()) {
                in 0f..7000f -> {
                    level = 1
                    desp = getString(R.string.title_little)
                }
                in 7000f..15000f -> {
                    level = 2
                    desp = getString(R.string.title_average)
                }
                in 15000f..30000f -> {
                    level = 3
                    desp = getString(R.string.title_good)
                }
                in 30000f..50000f -> {
                    level = 4
                    desp = getString(R.string.title_vice_rich)
                }
                else -> {
                    level = 5
                    desp = getString(R.string.title_rich)
                }
            }
            when (monthlyAmount) {
                in 0f..1500f -> {
                    bonusResult = amount * (1 - 0.03f)
                }
                in 1500f..4500f -> {
                    bonusResult = amount * (1 - 0.1f) + 105
                }
                in 4500f..9000f -> {
                    bonusResult = amount * (1 - 0.2f) + 555
                }
                in 9000f..35000f -> {
                    bonusResult = amount * (1 - 0.25f) + 10055
                }
                in 350000f..55000f -> {
                    bonusResult = amount * (1 - 0.3f) + 2755
                }
                in 550000f..80000f -> {
                    bonusResult = amount * (1 - 0.35f) + 5505
                }
                else -> {
                    bonusResult = amount * (1 - 0.45f) + 13505
                }
            }
            (resultView?.findViewById(R.id.ratingBar) as RatingBar).rating = level.toFloat()
            (resultView?.findViewById(R.id.ratingTitle) as TextView).text = desp
            if (bonusResult.compareTo(bonusResult.toInt()) != 0) {
                (resultView?.findViewById(R.id.value) as TextView).setText("%.2f".format(bonusResult))
            } else {
                (resultView?.findViewById(R.id.value) as TextView).setText("%.0f".format(bonusResult))
            }
            (resultView?.findViewById(R.id.valueDesp) as TextView).text = getString(R.string.bonus_after_tax) + " " + "%.0f".format(bonusResult * 100 / amount) + "%"
        }

        private fun hideKeyBoard(mEditText: EditText) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(mEditText.windowToken, 0)
        }


        override fun getCount(): Int {
            return 2
        }


        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View)
        }

    }

}
