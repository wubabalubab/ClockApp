package com.example.clockapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.Calendar

class CustomClockView : View {

    constructor(context: Context) : this(context, null)

    val bgCircleWidth = 10f
    var centerX = 10f
    var centerY = 10f
    var mWidth = 1000
    var mHeight = 1000
    var mRadius = 800f
    lateinit var mPaint: Paint
    lateinit var mTextPaint: Paint
    lateinit var mTxtPath: Path
    lateinit var rect: Rect

    constructor(context: Context?, attr: AttributeSet?) : super(context, attr) {
        initView()
    }


    private fun initView() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTxtPath = Path()
        rect = Rect()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            drawBg(it)
            drawClockScale(canvas, rect)
            drawPointer(canvas)
            drawCirclePoint(canvas)
            postInvalidateDelayed(1000)
        }

    }

    private fun drawBg(canvas: Canvas) {
        mPaint.strokeWidth = bgCircleWidth
        mPaint.color = Color.BLACK
        mPaint.style = Paint.Style.STROKE
        canvas.drawCircle(centerX, centerY, mRadius, mPaint)
    }

    private fun drawClockScale(canvas: Canvas, rect: Rect) {
        //短指针长度
        val shortHeight = 30
        //长指针长度
        val longHeight = 50
        //线宽度
        mPaint.strokeWidth = 10f
        //数字距离指针距离
        val txtScaleSpace = 60
        //数字字体颜色
        mTextPaint.color = Color.BLACK
        //数字字号
        mTextPaint.textSize = 40f
        for (index in 1..60) {
            canvas.rotate(6f, centerX, centerY)
            if (index % 5 == 0) {
                canvas.drawLine(centerX, centerY - mRadius, centerX, centerY - mRadius + longHeight, mPaint)
                mTextPaint.getTextBounds(index.toString(), 0, index.toString().length, rect)
                val textWidth = rect.width()
                val textHeight = rect.height()
                canvas.save()
                canvas.rotate((360 - index * 6).toFloat(), centerX, (longHeight + txtScaleSpace + textHeight / 2).toFloat())
                canvas.drawText((index / 5).toString(), centerX - textWidth / 2, centerY - mRadius + txtScaleSpace + longHeight, mTextPaint)
                canvas.restore()
            } else {
                canvas.drawLine(centerX, centerY - mRadius, centerX, centerY - mRadius + shortHeight, mPaint)
            }
        }
    }

    private fun drawPointer(canvas: Canvas) {
        val calender = Calendar.getInstance()
        val timeHour = calender[Calendar.HOUR]
        val timeMinute = calender[Calendar.MINUTE]
        val timeSecond = calender[Calendar.SECOND]
        val hourDegree = (timeHour / 12f) * 360;
        val minuteDegree = (timeMinute / 60f) * 360
        val secondDegree = (timeSecond / 60f) * 360
        val hourHeight = 240
        val minuteHeight = 270
        val secondHeight = 300
        val hourPointWidth = 30f
        val minutePointWidth = 25f
        val secondPointWidth = 20f


        mPaint.color = Color.BLACK
        mPaint.strokeWidth = hourPointWidth
        //绘制时针
        drawPointLine(mPaint, canvas, hourDegree, hourHeight)
        mPaint.color = Color.GREEN
        mPaint.strokeWidth = minutePointWidth
        drawPointLine(mPaint, canvas, minuteDegree, minuteHeight)
        mPaint.color = Color.RED
        mPaint.strokeWidth = secondPointWidth
        drawPointLine(mPaint, canvas, secondDegree, secondHeight)
    }

    private fun drawCirclePoint(canvas: Canvas) {
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, 30F, mPaint)
    }

    private fun drawPointLine(paint: Paint, canvas: Canvas, degree: Float, height: Int) {
        canvas.save()
        canvas.rotate(degree, centerX, centerY)
        canvas.drawLine(centerX, centerY - height, centerX, centerY, paint)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()
        mRadius = centerX - 40
    }


    companion object {
        private const val TAG = "CustomClockView"
    }

}