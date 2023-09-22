package com.arefdev.base.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomSelectableTextViewBinding
import com.arefdev.base.utils.ResourceManager.getFont

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class SelectableTextView : LinearLayout {

    private lateinit var binding: CustomSelectableTextViewBinding
    var text: CharSequence? = null
        private set
    private var inactiveStrokeColor = 0
    private var inactiveBackgroundColor = 0
    private var inactiveTextColor = 0
    private var activeStrokeColor = 0
    private var activeBackgroundColor = 0
    private var activeTextColor = 0
    var isActive = false
        set(active) {
            field = active
            updateView()
        }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(attrs: AttributeSet?) {
        binding = CustomSelectableTextViewBinding.inflate(LayoutInflater.from(context), this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectableTextView)

        setInactiveStrokeColor(
            typedArray
                .getColor(R.styleable.SelectableTextView_inactiveStrokeColor, ContextCompat.getColor(context, R.color.border))
        )

        setInactiveBackgroundColor(
            typedArray
                .getColor(R.styleable.SelectableTextView_inactiveBackgroundColor, ContextCompat.getColor(context, R.color.bg))
        )

        setInactiveTextColor(
            typedArray
                .getColor(R.styleable.SelectableTextView_inactiveTextColor, ContextCompat.getColor(context, R.color.text_on_bg))
        )

        setStrokeColor(
            typedArray
                .getColor(R.styleable.SelectableTextView_activeStrokeColor, ContextCompat.getColor(context, R.color.actionActive))
        )

        setBackgroundColor(
            typedArray
                .getColor(R.styleable.SelectableTextView_activeBackgroundColor, ContextCompat.getColor(context, R.color.actionActive))
        )

        setTextColor(
            typedArray
                .getColor(R.styleable.SelectableTextView_activeTextColor, ContextCompat.getColor(context, R.color.bg))
        )

        if (typedArray.hasValue(R.styleable.SelectableTextView_text))
            setText(
                typedArray
                    .getText(R.styleable.SelectableTextView_text)
            )

        if (typedArray.hasValue(R.styleable.SelectableTextView_textSize))
            setTextSize(
                typedArray
                    .getDimension(R.styleable.SelectableTextView_textSize, context.resources.getDimension(R.dimen.text_size_default))
            )

        if (typedArray.hasValue(R.styleable.SelectableTextView_textGravity))
            binding.tv.gravity = typedArray
                .getInt(R.styleable.SelectableTextView_textGravity, Gravity.START)

        setFontFamily(
            typedArray
                .getString(R.styleable.SelectableTextView_fontFamily)
        )

        if (typedArray.hasValue(R.styleable.SelectableTextView_isActive))
            isActive = typedArray.getBoolean(R.styleable.SelectableTextView_isActive, false)

        typedArray.recycle()

        binding.tv.setOnTouchListener { _: View?, event: MotionEvent? -> binding.bg.onTouchEvent(event) }
    }

    protected fun getInactiveStrokeColor(): Int {
        return inactiveStrokeColor
    }

    fun setInactiveStrokeColor(color: Int) {
        inactiveStrokeColor = color
        setStrokeColor()
    }

    protected fun getInactiveBackgroundColor(): Int {
        return inactiveBackgroundColor
    }

    fun setInactiveBackgroundColor(color: Int) {
        inactiveBackgroundColor = color
        setBackgroundColor()
    }

    protected fun getInactiveTextColor(): Int {
        return inactiveTextColor
    }

    fun setInactiveTextColor(color: Int) {
        inactiveTextColor = color
        setTextColor()
    }

    fun setStrokeColor(color: Int) {
        activeStrokeColor = color
        setStrokeColor()
    }

    override fun setBackgroundColor(color: Int) {
        activeBackgroundColor = color
        setBackgroundColor()
    }

    fun setTextColor(color: Int) {
        activeTextColor = color
        setTextColor()
    }

    fun setText(resId: Int) {
        text = context.getText(resId)
        setText()
    }

    fun setText(text: CharSequence?) {
        this.text = text
        setText()
    }

    fun setTextSize(size: Float) {
        binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setFontFamily(fontFamily: String?) {
        if (fontFamily == null) return
        if (isInEditMode) binding.tv.typeface = Typeface.create(fontFamily, Typeface.NORMAL) else binding.tv.typeface =
            getFont(context, fontFamily)
    }

    private fun setStrokeColor() {
        binding.bg.setStrokeColor(if (!isActive) inactiveStrokeColor else activeStrokeColor)
    }

    private fun setBackgroundColor() {
        binding.bg.setBackgroundColor(if (!isActive) inactiveBackgroundColor else activeBackgroundColor)
    }

    private fun setTextColor() {
        binding.tv.setTextColor(if (!isActive) inactiveTextColor else activeTextColor)
    }

    private fun setText() {
        binding.tv.text = text
    }

    override fun setGravity(gravity: Int) {
        binding.tv.gravity = gravity
    }

    protected fun updateView() {
        setStrokeColor()
        setBackgroundColor()
        setTextColor()
    }

    protected val bg: RoundedCornerLayout
        get() = binding.bg

    override fun setOnClickListener(l: OnClickListener?) {
        binding.bg.setOnClickListener(l)
    }

    override fun hasOnClickListeners(): Boolean {
        return binding.bg.hasOnClickListeners()
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        binding.bg.setOnLongClickListener(l)
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    override fun hasOnLongClickListeners(): Boolean {
        return binding.bg.hasOnLongClickListeners()
    }
}