package com.arefdev.base.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomPlaceholderTextViewBinding
import com.arefdev.base.utils.ResourceManager.getFont

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class PlaceholderTextView : FrameLayout {

    private lateinit var binding: CustomPlaceholderTextViewBinding
    private var placeholder: CharSequence? = null
    private var text: CharSequence? = null
    private var placeholderStrokeColor = 0
    private var placeholderBackgroundColor = 0
    private var placeholderTextColor = 0
    private var strokeColor = 0
    private var backgroundColor = 0
    private var textColor = 0

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CustomPlaceholderTextViewBinding.inflate(LayoutInflater.from(context), this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlaceholderTextView)

        setPlaceholderStrokeColor(
            typedArray
                .getColor(R.styleable.PlaceholderTextView_placeholderStrokeColor, ContextCompat.getColor(context, R.color.actionInactive))
        )

        setPlaceholderBackgroundColor(
            typedArray
                .getColor(R.styleable.PlaceholderTextView_placeholderBackgroundColor, ContextCompat.getColor(context, R.color.bg))
        )

        setPlaceholderTextColor(
            typedArray
                .getColor(R.styleable.PlaceholderTextView_placeholderTextColor, ContextCompat.getColor(context, R.color.actionInactive))
        )

        setStrokeColor(
            typedArray
                .getColor(R.styleable.PlaceholderTextView_strokeColor, ContextCompat.getColor(context, R.color.actionActive))
        )

        setBackgroundColor(
            typedArray
                .getColor(R.styleable.PlaceholderTextView_backgroundColor, ContextCompat.getColor(context, R.color.actionActive))
        )

        setTextColor(
            typedArray
                .getColor(R.styleable.PlaceholderTextView_textColor, ContextCompat.getColor(context, R.color.bg))
        )

        if (typedArray.hasValue(R.styleable.PlaceholderTextView_placeholderText))
            setPlaceholder(
                typedArray
                    .getText(R.styleable.PlaceholderTextView_placeholderText)
            )

        if (typedArray.hasValue(R.styleable.PlaceholderTextView_text))
            setText(
                typedArray
                    .getText(R.styleable.PlaceholderTextView_text)
            )

        if (typedArray.hasValue(R.styleable.PlaceholderTextView_fontFamily))
            setFontFamily(
                typedArray
                    .getString(R.styleable.PlaceholderTextView_fontFamily)
            )

        typedArray.recycle()
    }

    fun setPlaceholderStrokeColor(color: Int) {
        placeholderStrokeColor = color
        setStrokeColor()
    }

    fun setPlaceholderBackgroundColor(color: Int) {
        placeholderBackgroundColor = color
        setBackgroundColor()
    }

    fun setPlaceholderTextColor(color: Int) {
        placeholderTextColor = color
        setTextColor()
    }

    fun setStrokeColor(color: Int) {
        strokeColor = color
        setStrokeColor()
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        setBackgroundColor()
    }

    fun setTextColor(color: Int) {
        textColor = color
        setTextColor()
    }

    fun setPlaceholder(resId: Int) {
        placeholder = context.getText(resId)
        setText()
    }

    fun setPlaceholder(text: CharSequence?) {
        placeholder = text
        setText()
    }

    fun setText(resId: Int) {
        text = context.getText(resId)
        setText()
    }

    fun setText(text: CharSequence?) {
        this.text = text
        setText()
    }

    fun setFontFamily(fontFamily: String?) {
        if (isInEditMode)
            binding.tv.typeface = Typeface.create(fontFamily, Typeface.NORMAL)
        else
            binding.tv.typeface = getFont(context, fontFamily!!)
    }

    private val isEmpty: Boolean
        get() = text.isNullOrEmpty()

    private fun setStrokeColor() {
        binding.bg.setStrokeColor(if (isEmpty) placeholderStrokeColor else strokeColor)
    }

    private fun setBackgroundColor() {
        binding.bg.setBackgroundColor(if (isEmpty) placeholderBackgroundColor else backgroundColor)
    }

    private fun setTextColor() {
        binding.tv.setTextColor(if (isEmpty) placeholderTextColor else textColor)
    }

    private fun setText() {
        binding.tv.text = if (isEmpty) placeholder else text
        updateView()
    }

    private fun updateView() {
        setStrokeColor()
        setBackgroundColor()
        setTextColor()
    }
}