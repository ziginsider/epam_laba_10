package io.github.ziginsider.epam_laba_10

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View

const val COLOR_YELLOW = 16776960

class CustomView : View {

    private var color: Int? = null
    private var openRightEye: Boolean? = null
    private var openLeftEye: Boolean? = null
    private var smile: Int? = null
    private lateinit var paint: Paint

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        setupPaint()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(attrs)
        setupPaint()
    }


    private fun initAttrs(attrs: AttributeSet?) {

        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0)
        try {
            color = typedArray?.getColor(R.styleable.CustomView_emojiColor, COLOR_YELLOW)
            openRightEye = typedArray?.getBoolean(R.styleable.CustomView_emojiRightEyeOpen, true)
            openLeftEye = typedArray?.getBoolean(R.styleable.CustomView_emojiLeftEyeOpen, true)
            smile = typedArray?.getInt(R.styleable.CustomView_emojiSmile, 0)
        } finally {
            typedArray?.recycle()
        }
    }

    private fun setupPaint() {
        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = color ?: COLOR_YELLOW
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val radius = if (width <= height) (width / 2).toFloat() else (height / 2).toFloat()
        canvas?.drawCircle(radius, radius, radius, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }
}