package com.maxwell.mclib.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.maxwell.mclib.util.UnitUtil


/**
 * Created by maxwellma on 04/11/2017.
 */
class QuickIndexBar : View {

    var paint: Paint? = null

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)//消除锯齿
        paint?.color = Color.WHITE
        paint?.typeface = Typeface.DEFAULT_BOLD//字体加粗
        paint?.textSize = UnitUtil.dpToPix(context, 12f).toFloat()
    }

    constructor(context: Context) : this(context, null)

    private val LETTERS = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    private var mHeight: Float = 0.toFloat()//每个字母所占用空间的高度(比字母本身高度大)
    private var mWidth: Float = 0.toFloat()//每个字母所占用空间的宽度(比字母本身宽度大)

    private var lastIndex = -1//记录上次触摸的索引,用来判断索引发生了变化才更新界面

    override fun onDraw(canvas: Canvas) {
        for (i in 0..LETTERS.size - 1) {
            val letter = LETTERS[i]
            //绘制字母,根坐标是左下角
            val x = mWidth * 0.5f - paint!!.measureText(letter) * 0.5f//左下角的x坐标
            val bounds = Rect()
            paint?.getTextBounds(letter, 0, letter.length, bounds)//把字母本身的范围传到矩形bounds里面
            val y = mHeight * 0.5f + bounds.height() * 0.5f + i * mHeight//左下角的y坐标
            //invalidate()执行后,会调用此方法重新绘制一次,改变字体颜色
            paint?.color = if (i == lastIndex) Color.GRAY else Color.WHITE//如果当前手指处于该字母上,则改变字母的颜色
            canvas.drawText(LETTERS[i], x, y, paint)//绘制字体
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredHeight = measuredHeight//索引条总长度
        mHeight = measuredHeight * 1.0f / LETTERS.size//每个字母所占用空间的高度(比字母本身高度大)
        mWidth = measuredWidth * 1.0f//每个字母所占用空间的宽度(比字母本身宽度大)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val y: Float
        val currentIndex: Int
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                y = event.y//获取手指触摸的当前在索引条上的屏幕高度像素值
                currentIndex = (y / mHeight).toInt()//得到当前的索引值
                if (currentIndex != lastIndex) {//当索引值发生了变化才更新
                    if (currentIndex >= 0 && currentIndex < LETTERS.size) {//控制索引值的范围
                        lastIndex = currentIndex//记录当前的索引值
                        if (onLetterUpdateListener != null) {
                            onLetterUpdateListener!!.onLetterUpdate(LETTERS[currentIndex])//更新ListView查找到的item的位置
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                lastIndex = -1//抬手还原索引值
                onLetterUpdateListener!!.onHiddenLetter()//隐藏屏幕中间的提示字母
            }
            else -> {
            }
        }
        invalidate()//给lastIndex赋值后,再次绘制更新界面,把改变字体的颜色显示出来
        return true
    }

    private var onLetterUpdateListener: OnLetterUpdateListener? = null

    fun getOnLetterUpdateListener(): OnLetterUpdateListener? {
        return onLetterUpdateListener
    }

    fun setOnLetterUpdateListener(onLetterUpdateListener: OnLetterUpdateListener) {
        this.onLetterUpdateListener = onLetterUpdateListener
    }

    interface OnLetterUpdateListener {
        fun onLetterUpdate(letter: String)

        fun onHiddenLetter() //隐藏
    }
}