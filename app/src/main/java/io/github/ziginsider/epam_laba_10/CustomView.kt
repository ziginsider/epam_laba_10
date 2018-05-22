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
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.GestureDetector
import android.support.v4.view.GestureDetectorCompat

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
    private var paintHead = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
    }
    private var paintParts = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = STROKE_EYE
        color = Color.BLACK
    }
    private var rightEyeOval = RectF()
    private var leftEyeOval = RectF()
    private var smileHappyOval = RectF()
    private var smileSadOval = RectF()

    private val touchDetector: GestureDetectorCompat
            = GestureDetectorCompat(context, MyGestureListener())

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        setupAttrs(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        setupAttrs(attrs)
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
                it.drawSimpleArc(leftEyeOval, SWEEP_ANGLE_OPEN_EYE)
                it.drawSimpleArc(rightEyeOval, SWEEP_ANGLE_OPEN_EYE)
            } else {
                it.drawSimpleArc(leftEyeOval, SWEEP_ANGLE_CLOSE_EYE)
                it.drawSimpleArc(rightEyeOval, SWEEP_ANGLE_CLOSE_EYE)
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
                paintHead.color = color
                openEyesState = getBoolean(INSTANCE_OPEN_EYES)
                smile = getInt(INSTANCE_SMILE)
            }
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (touchDetector.onTouchEvent(event)) return true
        return super.onTouchEvent(event)
    }

    internal inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.i("TAG", "onSingleTapUp")
            smile = if (smile == HAPPY) SAD else HAPPY
            invalidate()
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            Log.i("TAG", "onDown")
            return true
        }
    }

    private fun Canvas.drawSimpleArc(oval: RectF, angle: Float) {
        drawArc(oval, START_ANGLE_EYE, angle, false, paintParts)
    }

    private fun generateArcOvalF(x: Float, y: Float, radius: Float)
            = RectF(x - radius, y - radius, x + radius, y + radius)

    override fun setColor(color: Int) {
        this.color = color
        paintHead.color = this.color
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

    companion object {

        @IntDef(HAPPY.toLong(), SAD.toLong())
        @Retention(AnnotationRetention.SOURCE)
        annotation class Smile

        const val HAPPY = 0
        const val SAD = 1

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
    }
}
