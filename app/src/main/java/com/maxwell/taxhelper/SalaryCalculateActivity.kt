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
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.maxwell.jazzyviewpager.JazzyViewPager
import com.maxwell.mclib.Location.LocationHelper
import com.maxwell.mclib.util.KeyBoardUtils
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.Router
import com.maxwell.projectfoundation.provider.ConfigProvider
import com.maxwell.projectfoundation.util.FontUtil
import com.maxwell.projectfoundation.util.ToastUtil
import com.maxwell.taxhelper.bean.CityParams
import com.maxwell.taxhelper.busevent.BuchonggjjEvent
import com.maxwell.taxhelper.busevent.CitySwitch
import com.maxwell.taxhelper.widget.GjjParamSelector
import com.tendcloud.tenddata.TCAgent
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import de.greenrobot.event.ThreadMode
import kotlinx.android.synthetic.main.activity_annual_bonus.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by maxwellma on 14/09/2017.
 */
class SalaryCalculateActivity : BaseActivity() {

    var frontView: View? = null
    var pageIndex = 0
    var cityParams: CityParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annual_bonus)
        initViews()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        TCAgent.onPageStart(this, "monthSalary")
    }

    override fun onDestroy() {
        super.onDestroy()
        TCAgent.onPageEnd(this, "monthSalary")
        EventBus.getDefault().unregister(this)
    }

    private fun initViews() {
        (this.jazzyPager as JazzyViewPager).adapter = MainAdapter(this)
        (this.jazzyPager as JazzyViewPager).pageMargin = 0
        (this.jazzyPager as JazzyViewPager).setTransitionEffect(JazzyViewPager.TransitionEffect.CubeOut)
        (this.jazzyPager as JazzyViewPager).setPagingEnabled(false)
        (this.jazzyPager as JazzyViewPager).setScrollDuration(400)

    }

    inner class MainAdapter(context: Context) : PagerAdapter() {

        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View)
        }

        var level: Int = 1
        var salaryResult: Double = 0.0
        var rankTitle: String? = null
        var context: Context = context
        var resultView: View? = null
        var mChart: PieChart? = null

        override fun getCount(): Int {
            return 2
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            when (position) {
                0 -> {
                    frontView = LayoutInflater.from(context).inflate(R.layout.layout_salary_front_face, container, false)
                    frontView!!.findViewById(R.id.title_back).setOnClickListener(onBackClickedListener)
                    var salaryInput = frontView?.findViewById(R.id.salaryInput) as EditText
                    var cityText = frontView?.findViewById(R.id.city) as TextView
                    (frontView!!.findViewById(R.id.buchongzfgjj_param) as TextView).text = getString(R.string.buchongzfgjj) + "：0%"
                    if (StringUtils.isEmpty(LocationHelper.getCity(this@SalaryCalculateActivity))) {
                        ToastUtil.show(this@SalaryCalculateActivity, R.string.location_null, Toast.LENGTH_LONG)
                    } else {
                        cityText.text = LocationHelper.getCity(this@SalaryCalculateActivity)
                        cityParams = CityParamsProvider.getInstance(this@SalaryCalculateActivity).getCityParams(cityText.text.toString())
                        if (cityParams == null) {
                            ToastUtil.show(this@SalaryCalculateActivity, R.string.location_not_supported, Toast.LENGTH_LONG)
                        }
                    }
                    cityText.setOnClickListener { _ -> Router.getInstance().route(this@SalaryCalculateActivity, "cityList", null) }
                    FontUtil.setNumberFont(context, salaryInput)
                    salaryInput.setOnEditorActionListener { _, actionId, _ ->
                        when (actionId) {
                            EditorInfo.IME_ACTION_DONE -> frontView!!.findViewById(R.id.submit).performClick()
                        }
                        true
                    }
                    salaryInput.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(editable: Editable?) {
                            if (editable != null && editable.toString().isNotEmpty()) {
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
                    frontView?.findViewById(R.id.buchongzfgjj_frame)?.setOnClickListener { _ ->
                        KeyBoardUtils.hideKeyboard(this@SalaryCalculateActivity, salaryInput)
                        GjjParamSelector(this@SalaryCalculateActivity).show(salaryInput)
                    }
                    frontView?.findViewById(R.id.submit)?.setOnClickListener { _ ->
                        if (!TextUtils.isEmpty(salaryInput.text.toString())) {
                            KeyBoardUtils.hideKeyboard(this@SalaryCalculateActivity, salaryInput)
                            if (cityParams == null) {
                                ToastUtil.show(this@SalaryCalculateActivity, R.string.location_not_supported, Toast.LENGTH_LONG)
                            } else {
                                calculateSalary(salaryInput, cityText.text.toString())
                                initChart(salaryInput.text.toString().toDouble(), cityText.text.toString())
                                salaryInput.clearFocus()
                                jazzyPager.postDelayed({
                                    jazzyPager.setCurrentItem(1, true)
                                    pageIndex = 1
                                    salaryInput.setText("")
                                }, 500)
                            }
                        }
                    }
                    container?.addView(frontView!!)
                    (this@SalaryCalculateActivity.jazzyPager as JazzyViewPager).setObjectForPosition(frontView!!, position)
                    return frontView!!
                }
                else -> {
                    resultView = LayoutInflater.from(context).inflate(R.layout.layout_tax_result, container, false)
                    mChart = resultView!!.findViewById(R.id.chart) as PieChart
                    container?.addView(resultView)
                    (resultView?.findViewById(R.id.back))?.setOnClickListener { _ ->
                        (this@SalaryCalculateActivity.jazzyPager as JazzyViewPager).setCurrentItem(0, true)
                        pageIndex = 0
                    }
                    (this@SalaryCalculateActivity.jazzyPager as JazzyViewPager).setObjectForPosition(resultView, position)
                    return resultView!!
                }
            }
        }

        private fun initChart(amount: Double, city: String) {
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

            var societyInsuranceAmount = paySocietyInsurance(amount, cityParams!!)
            var taxAmount = payTax(amount, societyInsuranceAmount)
            var insuranceEntry = PieEntry("%.1f".format(societyInsuranceAmount * 100 / amount).toFloat(), "五险一金")
            var salaryEntry = PieEntry("%.1f".format((amount - taxAmount - societyInsuranceAmount) * 100 / amount).toFloat(), "到手薪资")
            var taxEntry = PieEntry(100 - salaryEntry.y - insuranceEntry.y, "个人所得税")
            fillChartData(arrayListOf(salaryEntry, insuranceEntry, taxEntry))
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

        private fun calculateSalary(mEditText: EditText, city: String) {
            if (StringUtils.isEmpty(mEditText.text.toString())) {
                return
            }
            var amount = mEditText.text.toString().toDouble()
            when (mEditText.text.toString().toFloat()) {
                in 0..7000 -> {
                    level = 1
                    rankTitle = getString(R.string.title_little)
                }
                in 7000..20000 -> {
                    level = 2
                    rankTitle = getString(R.string.title_average)
                }
                in 20000..30000 -> {
                    level = 3
                    rankTitle = getString(R.string.title_good)
                }
                in 30000..50000 -> {
                    level = 4
                    rankTitle = getString(R.string.title_vice_rich)
                }
                else -> {
                    level = 5
                    rankTitle = getString(R.string.title_rich)
                }
            }
            var societyInsuranceAmount = paySocietyInsurance(amount, cityParams!!)
            salaryResult = amount - payTax(amount, societyInsuranceAmount) - societyInsuranceAmount
            (resultView?.findViewById(R.id.ratingBar) as RatingBar).rating = level.toFloat()
            (resultView?.findViewById(R.id.ratingTitle) as TextView).text = rankTitle
            var resultText = (resultView!!.findViewById(R.id.value) as TextView)
            FontUtil.setNumberFont(this@SalaryCalculateActivity, resultText)
            if (salaryResult.compareTo(salaryResult.toInt()) != 0) {
                resultText.text = "%.2f".format(salaryResult)
            } else {
                resultText.text = "%.0f".format(salaryResult)
            }
            (resultView?.findViewById(R.id.valueDesp) as TextView).text = getString(R.string.bonus_after_tax) + " " + "%.1f".format(resultText.text.toString().toDouble() * 100 / amount) + "%"

            (resultView!!.findViewById(R.id.yanglao_gr) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.p_pension, 0)
            (resultView!!.findViewById(R.id.yanglao_gs) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.c_pension, 0)
            (resultView!!.findViewById(R.id.yiliao_gr) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.p_medical, 0)
            (resultView!!.findViewById(R.id.yiliao_gs) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.c_medical, 0)
            (resultView!!.findViewById(R.id.shiye_gr) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.p_unemployed, 0)
            (resultView!!.findViewById(R.id.shiye_gs) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.c_unemployed, 0)
            (resultView!!.findViewById(R.id.jichuzfgjj_gr) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.p_housing, 1)
            (resultView!!.findViewById(R.id.jichuzfgjj_gs) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.c_housing, 1)
            (resultView!!.findViewById(R.id.buchongzfgjj_gr) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.p_housing_extra, 1)
            (resultView!!.findViewById(R.id.buchongzfgjj_gs) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.c_housing_extra, 1)
            (resultView!!.findViewById(R.id.gongshang_gr) as TextView).text = getTaxDisplayStr(cityParams!!, amount, 0.0, 0)
            (resultView!!.findViewById(R.id.gongshang_gs) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.c_injury, 0)
            (resultView!!.findViewById(R.id.shengyu_gr) as TextView).text = getTaxDisplayStr(cityParams!!, amount, 0.0, 0)
            (resultView!!.findViewById(R.id.shengyu_gs) as TextView).text = getTaxDisplayStr(cityParams!!, amount, cityParams!!.insurance.c_maternity, 0)
            (resultView!!.findViewById(R.id.wuxian_gr) as TextView).text = "%.2f".format(paySocietyInsurance(amount, cityParams!!))
            (resultView!!.findViewById(R.id.wuxian_gs) as TextView).text = "%.2f".format(payCompanySocietyInsurance(amount, cityParams!!))

            (resultView!!.findViewById(R.id.excludeWuxian) as TextView).text = "%.2f".format(amount - paySocietyInsurance(amount, cityParams!!))
            (resultView!!.findViewById(R.id.tax) as TextView).text = "%.2f".format(payTax(amount, societyInsuranceAmount))
            (resultView!!.findViewById(R.id.finalSalary) as TextView).text = "%.2f".format(amount - payTax(amount, societyInsuranceAmount) - societyInsuranceAmount)

            if (ConfigProvider.getInstance().salaryConfig != null && !ConfigProvider.getInstance().salaryConfig?.title.isNullOrEmpty()) {
                resultView!!.findViewById(R.id.forthPart).visibility = View.VISIBLE
                (resultView!!.findViewById(R.id.title) as TextView).text = ConfigProvider.getInstance().salaryConfig?.title
                if(!ConfigProvider.getInstance().salaryConfig?.actionText.isNullOrEmpty()) {
                    resultView!!.findViewById(R.id.actionText).visibility = View.VISIBLE
                    (resultView!!.findViewById(R.id.actionText) as TextView).text = ConfigProvider.getInstance().salaryConfig?.actionText
                } else {
                    (resultView!!.findViewById(R.id.title) as TextView).textSize = 14f
                    (resultView!!.findViewById(R.id.title) as TextView).setTextColor(Color.parseColor("#8c8c8c"))
                    resultView!!.findViewById(R.id.forthPartBg).setBackgroundColor(resources.getColor(android.R.color.white))
                }
                if (!ConfigProvider.getInstance().salaryConfig?.jumpUrl.isNullOrEmpty()) {
                    resultView!!.findViewById(R.id.forthPart).setOnClickListener { _ -> Router.getInstance().route(this@SalaryCalculateActivity, ConfigProvider.getInstance().salaryConfig?.jumpUrl!!) }
                }
            }
        }
    }

    private fun getTaxDisplayStr(cityParams: CityParams, amount: Double, factor: Double, type: Int): String {
        var result = 0.0
        when (type) {
            0 -> {
                when (amount) {
                    in 0.0..cityParams.insurance.minSocial -> result += cityParams.insurance.minSocial * factor
                    in cityParams.insurance.minSocial..cityParams.insurance.maxSocial -> result += amount * factor
                    else -> result += cityParams.insurance.maxSocial * factor
                }
            }
            1 -> {
                when (amount) {
                    in 0.0..cityParams.insurance.minFunds -> result += cityParams.insurance.minFunds * factor
                    in cityParams.insurance.minFunds..cityParams.insurance.maxFunds -> result += amount * factor
                    else -> result += cityParams.insurance.maxFunds * factor
                }
            }
        }
        return "%.2f".format(result) + " (" + "%.1f".format(factor * 100) + "%)"
    }


    private fun paySocietyInsurance(amount: Double, cityParams: CityParams): Double {
        var result = 0.0
        when (amount) {
            in 0.0..cityParams.insurance.minFunds -> result += cityParams.insurance.minFunds * (cityParams.insurance.p_housing + cityParams.insurance.p_housing_extra)
            in cityParams.insurance.minFunds..cityParams.insurance.maxFunds -> result += amount * (cityParams.insurance.p_housing + cityParams.insurance.p_housing_extra)
            else -> result += cityParams.insurance.maxFunds * (cityParams.insurance.p_housing + cityParams.insurance.p_housing_extra)
        }
        when (amount) {
            in 0.0..cityParams.insurance.minSocial -> result += cityParams.insurance.minSocial * (cityParams.insurance.p_pension + cityParams.insurance.p_medical + cityParams.insurance.p_unemployed + 0.0 + 0.0)
            in cityParams.insurance.minSocial..cityParams.insurance.maxSocial -> result += amount * (cityParams.insurance.p_pension + cityParams.insurance.p_medical + cityParams.insurance.p_unemployed + 0.0 + 0.0)
            else -> result += cityParams.insurance.maxSocial * (cityParams.insurance.p_pension + cityParams.insurance.p_medical + cityParams.insurance.p_unemployed + 0.0 + 0.0)
        }
        return result
    }

    private fun payCompanySocietyInsurance(amount: Double, cityParams: CityParams): Double {
        var result: Double = 0.0
        when (amount) {
            in 0.0..cityParams.insurance.minFunds -> result += cityParams.insurance.minFunds * (cityParams.insurance.c_housing + cityParams.insurance.c_housing_extra)
            in cityParams.insurance.minFunds..cityParams.insurance.maxFunds -> result += amount * (cityParams.insurance.c_housing + cityParams.insurance.c_housing_extra)
            else -> result += cityParams.insurance.maxFunds * (cityParams.insurance.c_housing + cityParams.insurance.c_housing_extra)
        }
        when (amount) {
            in 0.0..cityParams.insurance.minSocial -> result += cityParams.insurance.minSocial * (cityParams.insurance.c_pension + cityParams.insurance.c_medical + cityParams.insurance.c_unemployed + cityParams.insurance.c_maternity + cityParams.insurance.c_injury)
            in cityParams.insurance.minSocial..cityParams.insurance.maxSocial -> result += amount * (cityParams.insurance.c_pension + cityParams.insurance.c_medical + cityParams.insurance.c_unemployed + cityParams.insurance.c_maternity + cityParams.insurance.c_injury)
            else -> result += cityParams.insurance.maxSocial * (cityParams.insurance.c_pension + cityParams.insurance.c_medical + cityParams.insurance.c_unemployed + cityParams.insurance.c_maternity + cityParams.insurance.c_injury)
        }
        return result
    }

    private fun payTax(amount: Double, societyInsurance: Double): Double {
        var result: Double
        var subtractedAmount = amount - societyInsurance - 3500
        when (subtractedAmount) {
            in -1000000000..0 -> result = 0.0
            in 0..1500 -> result = subtractedAmount * 0.03
            in 1500..4500 -> result = subtractedAmount * 0.1 - 105
            in 4500..9000 -> result = subtractedAmount * 0.2 - 555
            in 9000..35000 -> result = subtractedAmount * 0.25 - 1005
            in 35000..55000 -> result = subtractedAmount * 0.3 - 2755
            in 55000..80000 -> result = subtractedAmount * 0.35 - 5505
            else -> result = subtractedAmount * 0.45 - 13505
        }
        return result
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    fun onCityEvent(citySwitch: CitySwitch) {
        (frontView!!.findViewById(R.id.city) as TextView).text = citySwitch.newCity.name + "市"
        var housing_extra = cityParams!!.insurance.p_housing_extra
        cityParams = CityParamsProvider.getInstance(this@SalaryCalculateActivity).getCityParams(citySwitch.newCity.name)
        cityParams!!.insurance.p_housing_extra = housing_extra
        cityParams!!.insurance.c_housing_extra = housing_extra
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    fun onGjjEvent(gjj: BuchonggjjEvent) {
        (frontView!!.findViewById(R.id.buchongzfgjj_param) as TextView).text = getString(R.string.buchongzfgjj) + "：" + gjj.params.toString() + "%"
        cityParams!!.insurance.p_housing_extra = gjj.params.toDouble() / 100
        cityParams!!.insurance.c_housing_extra = gjj.params.toDouble() / 100
    }

    override fun onBackPressed() {
        when (pageIndex) {
            0 -> super.onBackPressed()
            1 -> {
                jazzyPager.setCurrentItem(0, true)
                pageIndex = 0
            }
        }
    }

}
