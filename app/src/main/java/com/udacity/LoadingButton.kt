package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when (buttonState) {
            ButtonState.Loading -> startAnimation()

            ButtonState.Completed -> { valueAnimator.cancel()
                progress = 0f
                invalidate()}
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    private var progress: Float = 0f

    private var textColor = 0

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.LoadingButton
        ) {
            paint.color = getColor(R.styleable.LoadingButton_colorBackground, 0)
            textColor = getColor(R.styleable.LoadingButton_colorText,0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (buttonState) {
            ButtonState.Completed -> drawDefaultState(canvas)
            ButtonState.Loading -> drawLoadingState(canvas)
        }
    }

    private fun drawDefaultState(canvas: Canvas?) {
        paint.color = context.getColor(R.color.colorPrimary)
        canvas?.drawRect(
            0.0f,
            0.0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )

        paint.color = textColor
        canvas?.drawText(
            context.getString(R.string.button_name),
            width / 2.0f, (height +20) / 2.0f,
            paint
        )
    }

    private fun drawLoadingState(canvas: Canvas?) {
        paint.color = context.getColor(R.color.colorPrimary)
        canvas?.drawRect(
            0.0f,
            0.0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )

        paint.color = context.getColor(R.color.colorPrimaryDark)
        canvas?.drawRect(
            0f, 0f,
            (width * (progress / 100)).toFloat(), height.toFloat(), paint
        )

        paint.color = textColor
        canvas?.drawText(
            context.getString(R.string.button_loading),
            width / 2.0f, (height +20) / 2.0f,
            paint
        )

        paint.color = context.getColor(R.color.colorAccent)
        val textSize = paint.measureText(R.string.button_loading.toString())
        val x = (width.toFloat() - ((width.toFloat() - textSize) / 4))
        val y = height.toFloat() / 2.0f
        val r = height.toFloat() / 6.0f
        val left = x - r
        val right = x + r
        val top = y - r
        val bottom = y + r

        canvas?.drawArc( left,top, right,bottom,0f, (360 * (progress / 100)), true, paint)
    }

    fun startAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0f, 100f)
        valueAnimator.addUpdateListener {
            progress = it.animatedValue as Float
            paint.color = context.getColor(R.color.colorPrimary)
            invalidate()
        }
        valueAnimator.duration = 2000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.start()
    }

    fun setState(state: ButtonState) {
        buttonState = state
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}