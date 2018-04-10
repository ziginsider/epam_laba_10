package io.github.ziginsider.epam_laba_10

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Constants for drawing
 */
const val STROKE_EYE = 5F
const val START_ANGLE_EYE = 15F
const val SWEEP_ANGLE_CLOSE_EYE = 150F
const val SWEEP_ANGLE_OPEN_EYE = 360F
const val START_ANGLE_SMILE = 30F
const val SWEEP_ANGLE_SMILE = 120F
/**
 * Constants for instance saving
 */
const val INSTANCE_STATE = "instanceState"
const val INSTANCE_COLOR = "instanceColor"
const val INSTANCE_RIGHT_EYE = "instanceRightEye"
const val INSTANCE_LEFT_EYE = "instanceLeftEye"
const val INSTANCE_SMILE = "instanceSmile"

/**
 * Implementation View emoji smiley with opened and closed eyes and happy and sad smile.
 * Smiley can have different head color.
 *
 * <p>Opened methods to interact with View place in {@link EmojiSmiley}. View implements view state
 * saving and restoration. View implements internal click handler, which changes smile from the sad
 * one to the happy one and vice versa.
 *
 * @since 2018-04-10
 * @author Alex Kisel
 */
class CustomView : View, EmojiSmiley {
    private var color: Int = 0
    private var openRightEye: Boolean = false
    private var openLeftEye: Boolean = false
    private var smile: Int = 0
    private var radius: Float = 0F
    private lateinit var paintHead: Paint
    private lateinit var paintParts: Paint
    private lateinit var rightEyeOval: RectF
    private lateinit var leftEyeOval: RectF
    private lateinit var smileHappyOval: RectF
    private lateinit var smileSadOval: RectF

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        setupAttrs(attrs)
        setupPaint()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        setupAttrs(attrs)
        setupPaint()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawCircle(radius, radius, radius, paintHead)

            if (openRightEye) drawSimpleArc(it, rightEyeOval, SWEEP_ANGLE_OPEN_EYE)
            else drawSimpleArc(it, rightEyeOval, SWEEP_ANGLE_CLOSE_EYE)

            if (openLeftEye) drawSimpleArc(it, leftEyeOval, SWEEP_ANGLE_OPEN_EYE)
            else drawSimpleArc(it, leftEyeOval, SWEEP_ANGLE_CLOSE_EYE)

            if (smile == 0)
                it.drawArc(smileHappyOval, START_ANGLE_SMILE, SWEEP_ANGLE_SMILE, false, paintParts)
            else it.drawArc(smileSadOval, -1 * START_ANGLE_SMILE, -1 * SWEEP_ANGLE_SMILE,
                    false, paintParts)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupSizes(w, h)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putInt(INSTANCE_COLOR, color)
        bundle.putBoolean(INSTANCE_RIGHT_EYE, openRightEye)
        bundle.putBoolean(INSTANCE_LEFT_EYE, openLeftEye)
        bundle.putInt(INSTANCE_SMILE, smile)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            with(state) {
                color = getInt(INSTANCE_COLOR)
                setupPaint()
                openRightEye = getBoolean(INSTANCE_RIGHT_EYE)
                openLeftEye = getBoolean(INSTANCE_LEFT_EYE)
                smile = getInt(INSTANCE_SMILE)
            }
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = super.onTouchEvent(event)
        if (event?.action == MotionEvent.ACTION_DOWN) {
            smile = if (smile == 0) 1 else 0
            invalidate()
            return true
        }
        return result
    }

    private fun setupPaint() {
        paintHead = Paint()
        paintHead.style = Paint.Style.FILL
        paintHead.color = color
        paintParts = Paint()
        paintParts.style = Paint.Style.STROKE
        paintParts.strokeWidth = STROKE_EYE
        paintParts.color = Color.BLACK
    }

    private fun setupAttrs(attrs: AttributeSet?) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0)
        try {
            typedArray?.let {
                Log.d("TAG", "initAttr")
                color = it.getColor(R.styleable.CustomView_emojiColor, Color.YELLOW)
                openRightEye = it.getBoolean(R.styleable.CustomView_emojiRightEyeOpen, true)
                openLeftEye = it.getBoolean(R.styleable.CustomView_emojiLeftEyeOpen, true)
                smile = it.getInt(R.styleable.CustomView_emojiSmile, 0)
            }
        } finally {
            typedArray?.recycle()
        }
    }

    private fun setupSizes(width: Int, height: Int) {
        radius = if (width <= height) (width / 2).toFloat() else (height / 2).toFloat()
        rightEyeOval = generateArcOvalF(radius / 1.5F, radius / 1.5F, radius / 4F)
        leftEyeOval = generateArcOvalF(2 * radius / 1.5F, radius / 1.5F, radius / 4F)
        smileHappyOval = generateArcOvalF(radius, radius, radius / 1.5F)
        smileSadOval = generateArcOvalF(radius, radius * 2, radius / 1.5F)
    }

    private fun drawSimpleArc(canvas: Canvas, oval: RectF, angle: Float) {
        canvas.drawArc(oval, START_ANGLE_EYE, angle, false, paintParts)
    }

    private fun generateArcOvalF(x: Float, y: Float, radius: Float)
            = RectF(x - radius, y - radius, x + radius, y + radius)

    override fun setColor(color: Int) {
        this.color = color
        setupPaint()
        invalidate()
    }

    override fun setLeftEyeOpen(state: Boolean) {
        openLeftEye = state
        invalidate()
    }

    override fun isLeftEyeOpen() = openLeftEye

    override fun setRightEyeOpen(state: Boolean) {
        openRightEye = state
        invalidate()
    }

    override fun isRightEyeOpen() = openRightEye

    override fun setSmileState(state: Boolean) {
        smile = if (state) 0 else 1
        invalidate()
    }

    override fun isSmileHappy() = smile == 0
}
