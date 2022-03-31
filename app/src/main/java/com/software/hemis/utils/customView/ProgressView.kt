package com.software.hemis.utils.customView

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.hemis.R
import kotlin.properties.Delegates

class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteAnimateView(context, attrs, defStyleAttr) {

    private val defaultBgColor: Int = ContextCompat.getColor(context, R.color.blue_light)
    private val defaultBgStrokeColor: Int = ContextCompat.getColor(context, R.color.blue_light)
    private val defaultProgressColor: Int = ContextCompat.getColor(context, R.color.white)
    private val dp = resources.displayMetrics.density

    private val progressPadding = 4 * dp
    private val progressRect: RectF = RectF()
    private var bgRadius: Float = 0f
    private var quarterLeft: Float = 0F
    private var quarterTop: Float = 0F
    private var widthDisplay: Int = 0
    private var heightDisplay: Int = 0

    //in degrees [0, 360)
    private var currentAngle: Float by Delegates.observable(0f) { _, _, _ -> invalidate() }
    private var sweepAngle: Float by Delegates.observable(MIN_SWEEP_ANGLE) { _, _, _ -> invalidate() }
    private var bitmap: Bitmap by Delegates.observable(
        ContextCompat.getDrawable(
            context,
            R.drawable.ic_download
        )!!.toBitmap()
    ) { _, _, _ -> invalidate() }

    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = defaultProgressColor
    }
    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = defaultBgColor
    }
    private val bgStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = defaultBgStrokeColor
        strokeWidth = 0 * dp
    }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 2 * dp
        color = defaultProgressColor
    }

    private companion object {
        const val SPIN_DURATION_MS = 2_000L
        const val MIN_SWEEP_ANGLE = 0f //in degrees
        const val MAX_ANGLE = 360 //in degrees
    }

    @FloatRange(from = .0, to = 1.0, toInclusive = false)
    var progress: Float = 0f
        set(value) {
            field = when {
                value < 0f -> 0f
                value > 1f -> 1f
                else -> value
            }
            sweepAngle = convertToSweepAngle(field)
            invalidate()
        }

    var mBitmap: Bitmap = ContextCompat.getDrawable(context, R.drawable.ic_download)!!.toBitmap()
        set(value) {
            field = value
            bitmap = field
            invalidate()
        }

    override fun createAnimation(): Animator {
        return ValueAnimator.ofFloat(currentAngle, currentAngle + MAX_ANGLE).apply {
            interpolator = LinearInterpolator()
            duration = SPIN_DURATION_MS
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { currentAngle = normalize(it.animatedValue as Float) }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthDisplay = measuredWidth
        heightDisplay = measuredHeight

        val horizHalf = (measuredWidth) / 2f
        val vertHalf = (measuredHeight) / 2f

        quarterLeft = measuredWidth / 4F
        quarterTop = measuredHeight / 4F

        val progressOffset = progressPadding + progressPaint.strokeWidth / 2f

        //since the stroke it drawn on center of the line, we need to safe space for half of it, or it will be truncated by the bounds
        bgRadius = kotlin.math.min(horizHalf, vertHalf) - bgStrokePaint.strokeWidth / 2f

        val progressRectMinSize = 2 * (kotlin.math.min(horizHalf, vertHalf) - progressOffset)
        progressRect.apply {
            left = (measuredWidth - progressRectMinSize) / 2f
            top = (measuredHeight - progressRectMinSize) / 2f
            right = (measuredWidth + progressRectMinSize) / 2f
            bottom = (measuredHeight + progressRectMinSize) / 2f
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        with(canvas) {
            //(radius - strokeWidth) - because we don't want to overlap colors (since they by default translucent)
            drawCircle(
                progressRect.centerX(),
                progressRect.centerY(),
                bgRadius - bgStrokePaint.strokeWidth / 2f,
                bgPaint
            )
            drawCircle(progressRect.centerX(), progressRect.centerY(), bgRadius, bgStrokePaint)
            drawArc(progressRect, currentAngle, sweepAngle, false, progressPaint)
            drawBitmap(
                Bitmap.createScaledBitmap(bitmap, widthDisplay/2, heightDisplay/2, true),
                quarterLeft,
                quarterTop,
                bitmapPaint
            )
        }
    }

    /**
     * converts (shifts) the angle to be from 0 to 360.
     * For instance: if angle = 400.54, the normalized version will be 40.54
     * Note: angle = 360 will be normalized to 0
     */
    private fun normalize(angle: Float): Float {
        val decimal = angle - angle.toInt()
        return (angle.toInt() % MAX_ANGLE) + decimal
    }

    private fun convertToSweepAngle(progress: Float): Float =
        MIN_SWEEP_ANGLE + progress * (MAX_ANGLE - MIN_SWEEP_ANGLE)

}