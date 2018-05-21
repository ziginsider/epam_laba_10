package io.github.ziginsider.epam_laba_10

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface.NORMAL
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.IntDef
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
const val INSTANCE_OPEN_EYES = "instanceLeftEye"
const val INSTANCE_SMILE = "instanceSmile"

/**
 * Implementation View emoji smiley with opened and closed eyes and happy and sad smile.
 * Smiley can have different head color.
 *
 * Opened methods to interact with View place in [EmojiSmiley]. View implements view state
 * saving and restoration. View implements internal click handler, which changes smile from the sad
 * one to the happy one and vice versa.
 *
 * @since 2018-04-10
 * @author Alex Kisel
 */
class CustomView : View, EmojiSmiley {
    private var color = 0
    var openEyesState = false
        set(value) {
            field = value
            invalidate()
        }
    @Smile
    private var smile = HAPPY
    private var radius = 0F
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
                openEyesState = it.getBoolean(R.styleable.CustomView_emojiEyesOpen, true)
                smile = it.getInt(R.styleable.CustomView_emojiSmile, 0)
            }
        } finally {
            typedArray?.recycle()
        }
    }

    companion object {

        @IntDef(HAPPY.toLong(), SAD.toLong())
        @Retention(AnnotationRetention.SOURCE)
        annotation class Smile

        const val HAPPY = 0
        const val SAD = 1
    }

    private fun setupSizes(width: Int, height: Int) {
        radius = if (width <= height) (width / 2).toFloat() else (height / 2).toFloat()
        rightEyeOval = generateArcOvalF(radius / 1.5F, radius / 1.5F, radius / 4F)
        leftEyeOval = generateArcOvalF(2 * radius / 1.5F, radius / 1.5F, radius / 4F)
        smileHappyOval = generateArcOvalF(radius, radius, radius / 1.5F)
        smileSadOval = generateArcOvalF(radius, radius * 2, radius / 1.5F)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawCircle(radius, radius, radius, paintHead)

            if (openEyesState) {
                drawSimpleArc(it, leftEyeOval, SWEEP_ANGLE_OPEN_EYE)
                drawSimpleArc(it, rightEyeOval, SWEEP_ANGLE_OPEN_EYE)
            } else {
                drawSimpleArc(it, leftEyeOval, SWEEP_ANGLE_CLOSE_EYE)
                drawSimpleArc(it, rightEyeOval, SWEEP_ANGLE_CLOSE_EYE)
            }

            if (smile == HAPPY) {
                it.drawArc(smileHappyOval, START_ANGLE_SMILE, SWEEP_ANGLE_SMILE, false, paintParts)
            } else {
                it.drawArc(smileSadOval, -1 * START_ANGLE_SMILE, -1 * SWEEP_ANGLE_SMILE,
                        false, paintParts)
            }
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
        bundle.putBoolean(INSTANCE_OPEN_EYES, openEyesState)
        bundle.putInt(INSTANCE_SMILE, smile)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            with(state) {
                color = getInt(INSTANCE_COLOR)
                setupPaint()
                openEyesState = getBoolean(INSTANCE_OPEN_EYES)
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
            smile = if (smile == HAPPY) SAD else HAPPY
            invalidate()
            return true
        }
        return result
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

    override fun setEyesOpenState(state: Boolean) {
        openEyesState = state
        invalidate()
    }

    override fun areEyesOpen() = openEyesState

    override fun setSmileState(state: Boolean) {
        smile = if (state) HAPPY else SAD
        invalidate()
    }

    override fun isSmileHappy() = smile == HAPPY
}
