package com.maxwell.taxhelper

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.maxwell.jazzyviewpager.JazzyViewPager
import com.maxwell.mclib.util.KeyBoardUtils
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.Router
import com.maxwell.projectfoundation.provider.ConfigProvider
import com.maxwell.projectfoundation.util.FontUtil
import com.tendcloud.tenddata.TCAgent
import kotlinx.android.synthetic.main.activity_annual_bonus.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by maxwellma on 28/08/2017.
 */
class AnnualBonusActivity : BaseActivity() {

    var pageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annual_bonus)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        TCAgent.onPageStart(this, "annualBonus")
    }

    override fun onDestroy() {
        super.onDestroy()
        TCAgent.onPageEnd(this, "annualBonus")
    }

    private fun initViews() {
        (this.jazzyPager as JazzyViewPager).adapter = MainAdapter(this@AnnualBonusActivity)
        (this.jazzyPager as JazzyViewPager).pageMargin = 0
        (this.jazzyPager as JazzyViewPager).setTransitionEffect(JazzyViewPager.TransitionEffect.CubeOut)
        (this.jazzyPager as JazzyViewPager).setPagingEnabled(false)
        (this.jazzyPager as JazzyViewPager).setScrollDuration(400)

    }

    override fun onBackPressed() {
        when (pageIndex) {
            0 -> super.onBackPressed()
            else -> {
                jazzyPager.setCurrentItem(0, true)
                pageIndex = 0
            }
        }
    }

    inner class MainAdapter : PagerAdapter {

        var context: Context
        var level: Int = 1
        var bonusResult: Float = 0.0f
        var desp: String? = null
        var resultView: View? = null
        var mChart : PieChart? = null

        constructor(context: Context) {
            this.context = context
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            when (position) {
                0 -> {
                    var view = LayoutInflater.from(context).inflate(R.layout.layout_annual_front_face, container, false)
                    view!!.findViewById(R.id.title_back).setOnClickListener(onBackClickedListener)
                    var salaryInput = view.findViewById(R.id.salaryInput) as EditText
                    FontUtil.setNumberFont(context, salaryInput)
                    salaryInput.setOnEditorActionListener { _, actionId, _ ->
                        when (actionId) {
                            EditorInfo.IME_ACTION_DONE -> view.findViewById(R.id.submit).performClick()
                        }
                        true
                    }
                    salaryInput.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(editable: Editable?) {
                            if (editable != null && editable.toString().length > 0) {
                                salaryInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f)
                            } else {
                                salaryInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                            }
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                    })
                    view.findViewById(R.id.submit).setOnClickListener { _ ->
                        if (!TextUtils.isEmpty(salaryInput.text.toString())) {
                            KeyBoardUtils.hideKeyboard(this@AnnualBonusActivity, salaryInput)
                            salaryInput.clearFocus()
                            initChart()
                            calculateAward(salaryInput)
                            jazzyPager.postDelayed({ ->
                                jazzyPager.setCurrentItem(1, true)
                                pageIndex = 1
                                salaryInput.setText("")
                            }, 500)
                        }
                    }
                    container?.addView(view)
                    (this@AnnualBonusActivity.jazzyPager as JazzyViewPager).setObjectForPosition(view, position)
                    return view
                }
                else -> {
                    resultView = LayoutInflater.from(context).inflate(R.layout.layout_tax_result, container, false)
                    mChart = resultView!!.findViewById(R.id.chart) as PieChart?
                    (resultView?.findViewById(R.id.ratingBar) as RatingBar).rating = level.toFloat()
                    (resultView?.findViewById(R.id.value) as TextView).text = "%.2f".format(bonusResult)
                    resultView!!.findViewById(R.id.thirdPart).visibility = View.GONE
                    FontUtil.setNumberFont(context, resultView?.findViewById(R.id.value) as TextView)
                    container?.addView(resultView)
                    (resultView?.findViewById(R.id.back))?.setOnClickListener { _ ->
                        (this@AnnualBonusActivity.jazzyPager as JazzyViewPager).setCurrentItem(0, true)
                        pageIndex = 0
                    }
                    (this@AnnualBonusActivity.jazzyPager as JazzyViewPager).setObjectForPosition(resultView, position)
                    return resultView!!
                }
            }
        }

        private fun initChart() {
            mChart!!.setUsePercentValues(true)
            mChart!!.description.isEnabled = false
            mChart!!.setExtraOffsets(5f, 10f, 5f, 5f)

            mChart!!.dragDecelerationFrictionCoef = 0.95f

            mChart!!.isDrawHoleEnabled = true
            mChart!!.setHoleColor(Color.WHITE)

            mChart!!.setTransparentCircleColor(Color.WHITE)
            mChart!!.setTransparentCircleAlpha(110)

            mChart!!.holeRadius = 58f
            mChart!!.transparentCircleRadius = 61f

            mChart!!.setDrawCenterText(false)

            mChart!!.rotationAngle = 0f
            // enable rotation of the chart by touch
            mChart!!.isRotationEnabled = true
            mChart!!.isHighlightPerTapEnabled = true

            mChart!!.animateY(1400, Easing.EasingOption.EaseInOutQuad)
            mChart!!.setEntryLabelColor(Color.BLACK)
            mChart!!.setEntryLabelTextSize(12f)
            mChart!!.setDrawEntryLabels(false)
        }

        private fun fillChartData(entries: ArrayList<PieEntry>) {
            var dataSet = PieDataSet(entries, "")

            dataSet.setDrawIcons(false)

            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            dataSet.colors = arrayListOf(Color.parseColor("#FD907A"), Color.parseColor("#56CCBB"), Color.parseColor("#4189F5"), Color.parseColor("#E9F985"))

            //dataSet.setSelectionShift(0f);

            var data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.BLACK)
            mChart!!.data = data

            // undo all highlights
            mChart!!.highlightValues(null)

            mChart!!.invalidate()
        }

        private fun calculateAward(mEditText: EditText) {
            KeyBoardUtils.hideKeyboard(this@AnnualBonusActivity, mEditText)
            if (StringUtils.isEmpty(mEditText.text.toString())) {
                return
            }
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
                    bonusResult = amount * (1 - 0.25f) + 1005
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
                (resultView?.findViewById(R.id.value) as TextView).text = "%.2f".format(bonusResult)
            } else {
                (resultView?.findViewById(R.id.value) as TextView).text = "%.0f".format(bonusResult)
            }
            (resultView?.findViewById(R.id.valueDesp) as TextView).text = getString(R.string.bonus_after_tax) + " " + "%.1f".format(bonusResult * 100 / amount) + "%"

            initChart()
            var resultEntry = PieEntry("%.1f".format(bonusResult * 100 / amount).toFloat(), "税后年终奖")
            var taxEntry = PieEntry(100 - resultEntry.y, "个人所得税")
            fillChartData(arrayListOf(resultEntry, taxEntry))
            if (ConfigProvider.getInstance().bonusConfig != null && !ConfigProvider.getInstance().bonusConfig?.title.isNullOrEmpty()) {
                resultView!!.findViewById(R.id.forthPart).visibility = View.VISIBLE
                (resultView!!.findViewById(R.id.title) as TextView).text = ConfigProvider.getInstance().bonusConfig?.title
                if(!ConfigProvider.getInstance().salaryConfig?.actionText.isNullOrEmpty()) {
                    resultView!!.findViewById(R.id.actionText).visibility = View.VISIBLE
                    (resultView!!.findViewById(R.id.actionText) as TextView).text = ConfigProvider.getInstance().bonusConfig?.actionText
                } else {
                    (resultView!!.findViewById(R.id.title) as TextView).textSize = 14f
                    (resultView!!.findViewById(R.id.title) as TextView).setTextColor(Color.parseColor("#8c8c8c"))
                    resultView!!.findViewById(R.id.forthPartBg).setBackgroundColor(resources.getColor(android.R.color.white))
                }
                if (!ConfigProvider.getInstance().bonusConfig?.jumpUrl.isNullOrEmpty()) {
                    resultView!!.findViewById(R.id.forthPart).setOnClickListener { _ -> Router.getInstance().route(this@AnnualBonusActivity, ConfigProvider.getInstance().bonusConfig?.jumpUrl!!) }
                }
            }
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