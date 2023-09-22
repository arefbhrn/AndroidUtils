package com.arefdev.base.ui.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.arefdev.base.R
import com.arefdev.base.utils.dpToPx

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
open class RoundedCornerLayout : ConstraintLayout {

    companion object {
        private val DEFAULT_RADIUS = dpToPx(15f).toFloat()
    }

    private var radiusTopLeft = 0f
    private var radiusTopRight = 0f
    private var radiusBottomRight = 0f
    private var radiusBottomLeft = 0f
    private var strokeColor = 0
    private var strokeWidth = 0
    private var strokeDashWidth = 0
    private var strokeDashGap = 0
    private var bgColor = 0
    private var rippleColor = 0

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornerLayout)

        typedArray.getDimension(R.styleable.RoundedCornerLayout_radius, DEFAULT_RADIUS).let {
            radiusTopLeft = it
            radiusTopRight = it
            radiusBottomRight = it
            radiusBottomLeft = it
        }

        if (typedArray.hasValue(R.styleable.RoundedCornerLayout_radiusTopLeft))
            radiusTopLeft = typedArray.getDimension(R.styleable.RoundedCornerLayout_radiusTopLeft, DEFAULT_RADIUS)

        if (typedArray.hasValue(R.styleable.RoundedCornerLayout_radiusTopRight))
            radiusTopRight = typedArray.getDimension(R.styleable.RoundedCornerLayout_radiusTopRight, DEFAULT_RADIUS)

        if (typedArray.hasValue(R.styleable.RoundedCornerLayout_radiusBottomRight))
            radiusBottomRight = typedArray.getDimension(R.styleable.RoundedCornerLayout_radiusBottomRight, DEFAULT_RADIUS)

        if (typedArray.hasValue(R.styleable.RoundedCornerLayout_radiusBottomLeft))
            radiusBottomLeft = typedArray.getDimension(R.styleable.RoundedCornerLayout_radiusBottomLeft, DEFAULT_RADIUS)

        isEnabled = typedArray.getBoolean(R.styleable.RoundedCornerLayout_android_enabled, true)

        bgColor =
            typedArray.getColor(R.styleable.RoundedCornerLayout_backgroundColor, ContextCompat.getColor(context, R.color.colorPrimary))

        rippleColor = typedArray.getColor(R.styleable.RoundedCornerLayout_rippleColor, ContextCompat.getColor(context, R.color.black))

        strokeColor =
            typedArray.getColor(R.styleable.RoundedCornerLayout_strokeColor, ContextCompat.getColor(context, R.color.colorPrimaryDark))

        strokeWidth = typedArray.getDimension(R.styleable.RoundedCornerLayout_strokeWidth, 0f).toInt()

        strokeDashWidth = typedArray.getDimension(R.styleable.RoundedCornerLayout_strokeDashWidth, 0f).toInt()

        strokeDashGap = typedArray.getDimension(R.styleable.RoundedCornerLayout_strokeDashGap, 0f).toInt()

        typedArray.recycle()

        background = drawable
    }

    override fun isClickable(): Boolean {
        return isEnabled && super.isClickable()
    }

    override fun isLongClickable(): Boolean {
        return isEnabled && super.isLongClickable()
    }

    private val drawable: Drawable
        get() {
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadii = floatArrayOf(
                radiusTopLeft, radiusTopLeft,
                radiusTopRight, radiusTopRight,
                radiusBottomRight, radiusBottomRight,
                radiusBottomLeft, radiusBottomLeft
            )
            drawable.setColor(bgColor)
            drawable.setStroke(strokeWidth, strokeColor, strokeDashWidth.toFloat(), strokeDashGap.toFloat())
            return if (isClickable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                RippleDrawable(ColorStateList.valueOf(rippleColor), drawable, drawable)
            } else {
                drawable
            }
        }

    fun setRadius(radius: Float) {
        radiusTopLeft = radius
        radiusTopRight = radius
        radiusBottomRight = radius
        radiusBottomLeft = radius
        background = drawable
    }

    fun setRadius(topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
        radiusTopLeft = topLeftRadius
        radiusTopRight = topRightRadius
        radiusBottomRight = bottomRightRadius
        radiusBottomLeft = bottomLeftRadius
        background = drawable
    }

    override fun setBackgroundColor(color: Int) {
        bgColor = color
        background = drawable
    }

    fun getBackgroundColor(): Int {
        return bgColor
    }

    fun setStrokeColor(color: Int) {
        strokeColor = color
        background = drawable
    }

    fun getStrokeColor(): Int {
        return strokeColor
    }

    fun setStrokeWidth(width: Int) {
        strokeWidth = width
        background = drawable
    }

    fun getStrokeWidth(): Int {
        return strokeWidth
    }

    fun setStrokeDashWidth(width: Int) {
        strokeDashWidth = width
        background = drawable
    }

    fun getStrokeDashWidth(): Int {
        return strokeDashWidth
    }

    fun setStrokeDashGap(gap: Int) {
        strokeDashGap = gap
        background = drawable
    }

    fun getStrokeDashGap(): Int {
        return strokeDashGap
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        background = drawable
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        super.setOnLongClickListener(l)
        background = drawable
    }
}