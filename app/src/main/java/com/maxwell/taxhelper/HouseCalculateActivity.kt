package com.maxwell.taxhelper

import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.maxwell.jazzyviewpager.JazzyViewPager
import com.maxwell.mclib.Location.LocationHelper
import com.maxwell.mclib.util.KeyBoardUtils
import com.maxwell.projectfoundation.BaseActivity
import com.maxwell.projectfoundation.util.FontUtil
import com.maxwell.projectfoundation.util.ToastUtil
import com.maxwell.taxhelper.busevent.BuchonggjjEvent
import com.maxwell.taxhelper.widget.GjjParamSelector
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import de.greenrobot.event.ThreadMode
import kotlinx.android.synthetic.main.activity_annual_bonus.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by maxwellma on 05/12/2017.
 */
class HouseCalculateActivity : BaseActivity() {

    var frontView: View? = null
    var pageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annual_bonus)
        initViews()
        EventBus.getDefault().register(this)
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

        override fun getCount(): Int {
            return 2
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            when (position) {
                0 -> {
                    frontView = LayoutInflater.from(context).inflate(R.layout.layout_house_front_face, container, false)
                    frontView!!.findViewById(R.id.title_back).setOnClickListener(onBackClickedListener)
                    var debitInput = frontView?.findViewById(R.id.salaryInput) as EditText
                    (frontView!!.findViewById(R.id.buchongzfgjj_param) as TextView).text = getString(R.string.buchongzfgjj) + "：0%"
                    if (StringUtils.isEmpty(LocationHelper.getCity(this@HouseCalculateActivity))) {
                        ToastUtil.show(this@HouseCalculateActivity, R.string.location_null, Toast.LENGTH_LONG)
                    } else {
                        if (!CityListProvider.getInstance(this@HouseCalculateActivity).isCitySupported(LocationHelper.getCity(this@HouseCalculateActivity))) {
                            ToastUtil.show(this@HouseCalculateActivity, R.string.location_not_supported, Toast.LENGTH_LONG)
                        }
                    }
                    FontUtil.setNumberFont(context, debitInput)
                    debitInput.setOnEditorActionListener { _, actionId, _ ->
                        when (actionId) {
                            EditorInfo.IME_ACTION_DONE -> frontView!!.findViewById(R.id.submit).performClick()
                        }
                        true
                    }
                    debitInput.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(editable: Editable?) {
                            if (editable != null && editable.toString().isNotEmpty()) {
                                debitInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f)
                            } else {
                                debitInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                            }
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                    })
                    frontView?.findViewById(R.id.buchongzfgjj_frame)?.setOnClickListener { _ ->
                        KeyBoardUtils.hideKeyboard(this@HouseCalculateActivity, debitInput)
                        GjjParamSelector(this@HouseCalculateActivity).show(debitInput)
                    }
                    frontView?.findViewById(R.id.submit)?.setOnClickListener { _ ->
                        if (debitInput.text.toString().isNotEmpty()) {
                            KeyBoardUtils.hideKeyboard(this@HouseCalculateActivity, debitInput)
                            debitInput.clearFocus()
                            calculateDebit(debitInput, 0.0325, 240)
                            jazzyPager.postDelayed({
                                jazzyPager.setCurrentItem(1, true)
                                pageIndex = 1
                                debitInput.setText("")
                            }, 500)
                        }
                    }
                    container?.addView(frontView!!)
                    (this@HouseCalculateActivity.jazzyPager as JazzyViewPager).setObjectForPosition(frontView!!, position)
                    return frontView!!
                }
                else -> {
                    resultView = LayoutInflater.from(context).inflate(R.layout.layout_house_debit_result, container, false)
                    container?.addView(resultView)
                    (resultView?.findViewById(R.id.back))?.setOnClickListener { _ ->
                        (this@HouseCalculateActivity.jazzyPager as JazzyViewPager).setCurrentItem(0, true)
                        pageIndex = 0
                    }
                    (this@HouseCalculateActivity.jazzyPager as JazzyViewPager).setObjectForPosition(resultView, position)
                    return resultView!!
                }
            }
        }


        private fun calculateDebit(mEditText: EditText, interestRate: Double, months: Int) {
            if (StringUtils.isEmpty(mEditText.text.toString())) {
                return
            }
            var amount = mEditText.text.toString().toDouble()
            var repayPerMonth = (amount * interestRate / 12 * Math.pow((1 + interestRate / 12), months.toDouble())) / (Math.pow((1 + interestRate / 12), months.toDouble()) - 1)
            (resultView?.findViewById(R.id.debit_amount_bx) as TextView)?.text = "%.2f".format(amount)
            (resultView?.findViewById(R.id.debit_per_month_bx) as TextView)?.text = "%.2f".format(repayPerMonth)
            (resultView?.findViewById(R.id.total_amount_bx) as TextView)?.text = "%.2f".format(repayPerMonth * months)
            (resultView?.findViewById(R.id.total_interest_bx) as TextView)?.text = "%.2f".format(repayPerMonth * months - amount)
            (resultView?.findViewById(R.id.month_bx) as TextView)?.text = months.toString()

            var totalDebit = ((amount / months + amount * interestRate / 12) + amount / months * (1 + interestRate / 12)) / 2 * months
            var firstMonth = (amount / months) + amount * interestRate / 12
            var decending = "%.2f".format(amount / months * interestRate / 12)
            (resultView?.findViewById(R.id.debit_amount_bj) as TextView)?.text = "%.2f".format(amount)
            (resultView?.findViewById(R.id.debit_per_month_bj) as TextView)?.text = "首月: %.2f\n每月递减:$decending".format(firstMonth)
            (resultView?.findViewById(R.id.total_amount_bj) as TextView)?.text = "%.2f".format(totalDebit)
            (resultView?.findViewById(R.id.total_interest_bj) as TextView)?.text = "%.2f".format(totalDebit - amount)
            (resultView?.findViewById(R.id.month_bj) as TextView)?.text = months.toString()
        }
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    fun onGjjEvent(gjj: BuchonggjjEvent) {
        (frontView!!.findViewById(R.id.buchongzfgjj_param) as TextView).text = getString(R.string.buchongzfgjj) + "：" + gjj.params.toString() + "%"
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