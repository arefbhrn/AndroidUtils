package com.arefdev.base.ui.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomTitledTextViewBinding

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class TitledTextView : FrameLayout {

    private lateinit var binding: CustomTitledTextViewBinding

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CustomTitledTextViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (binding.tv.text.isEmpty()) {
                    binding.tvUnit.visibility = GONE
                } else if (binding.tv.text.isNotEmpty() && binding.tvUnit.visibility == GONE && binding.tvUnit.text.isNotEmpty()) {
                    binding.tvUnit.visibility = VISIBLE
                }
            }
        })

        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitledTextView)

        if (typedArray.hasValue(R.styleable.TitledTextView_labelText))
            setLabelText(
                typedArray.getText(R.styleable.TitledTextView_labelText)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_labelTextColor))
            setLabelTextColor(
                typedArray.getColor(R.styleable.TitledTextView_labelTextColor, Color.BLACK)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_labelTextSize))
            setLabelTextSize(
                typedArray.getDimension(R.styleable.TitledTextView_labelTextSize, context.resources.getDimension(R.dimen.text_size_default))
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_unitText))
            setUnitText(
                typedArray.getText(R.styleable.TitledTextView_unitText)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_unitTextColor))
            setUnitTextColor(
                typedArray.getColor(R.styleable.TitledTextView_unitTextColor, Color.BLACK)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_unitTextSize))
            setUnitTextSize(
                typedArray.getDimension(R.styleable.TitledTextView_unitTextSize, context.resources.getDimension(R.dimen.text_size_default))
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_android_textColor))
            setTextColor(
                typedArray.getColor(R.styleable.TitledTextView_android_textColor, Color.BLACK)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_android_textSize))
            setTextSize(
                typedArray.getDimension(R.styleable.TitledTextView_android_textSize, context.resources.getDimension(R.dimen.text_size_large))
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_textBackground))
            setTextBackgroundTint(
                typedArray.getInt(R.styleable.TitledTextView_textBackground, Color.TRANSPARENT)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_android_hint))
            setHint(
                typedArray.getText(R.styleable.TitledTextView_android_hint)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_android_textColorHint))
            setHintTextColor(
                typedArray.getColor(R.styleable.TitledTextView_android_textColorHint, Color.LTGRAY)
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_android_imeOptions))
            binding.tv.imeOptions = typedArray.getInt(R.styleable.TitledTextView_android_imeOptions, EditorInfo.IME_ACTION_NONE)

        if (typedArray.hasValue(R.styleable.TitledTextView_android_inputType))
            binding.tv.inputType = typedArray.getInt(R.styleable.TitledTextView_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)

        if (typedArray.hasValue(R.styleable.TitledTextView_android_lines))
            binding.tv.setLines(
                typedArray.getInt(R.styleable.TitledTextView_android_lines, 1)
            )

        binding.tv.minLines = typedArray.getInt(R.styleable.TitledTextView_android_minLines, 1)

        binding.tv.maxLines = typedArray.getInt(R.styleable.TitledTextView_android_maxLines, 1000000)

        if (typedArray.hasValue(R.styleable.TitledTextView_android_maxLength))
            binding.tv.filters = arrayOf<InputFilter>(
                InputFilter.LengthFilter(
                    typedArray.getInt(R.styleable.TitledTextView_android_maxLength, -1)
                )
            )

        if (typedArray.hasValue(R.styleable.TitledTextView_android_text))
            text = typedArray.getText(R.styleable.TitledTextView_android_text)

        if (typedArray.hasValue(R.styleable.TitledTextView_android_enabled))
            binding.tv.isEnabled = typedArray.getBoolean(R.styleable.TitledTextView_android_enabled, true)

        typedArray.recycle()
    }

    var text: CharSequence?
        get() = binding.tv.text
        set(text) {
            binding.tv.text = text
        }

    fun setTextColor(@ColorInt color: Int) {
        binding.tv.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setLabelText(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            binding.tvLabel.text = ""
            binding.tvLabel.visibility = GONE
        } else {
            binding.tvLabel.text = text
            binding.tvLabel.visibility = VISIBLE
        }
    }

    fun setLabelTextColor(@ColorInt color: Int) {
        binding.tvLabel.setTextColor(color)
    }

    fun setLabelTextSize(size: Float) {
        binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setUnitText(text: CharSequence?) {
        if (text.isNullOrEmpty())
            binding.tvUnit.text = ""
        else
            binding.tvUnit.text = text
    }

    fun setUnitTextColor(@ColorInt color: Int) {
        binding.tvUnit.setTextColor(color)
    }

    fun setUnitTextSize(size: Float) {
        binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setHint(text: CharSequence?) {
        binding.tv.hint = text
    }

    fun setHintTextColor(@ColorInt color: Int) {
        binding.tv.setHintTextColor(color)
    }

    fun addTextChangedListener(watcher: TextWatcher?) {
        binding.tv.addTextChangedListener(watcher)
    }

    val textView: TextView
        get() = binding.tv

    fun setTextBackgroundTint(color: Int) {
        ViewCompat.setBackgroundTintList(binding.lyt, ColorStateList.valueOf(color))
    }
}