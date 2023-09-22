package com.arefdev.base.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomRoundedButtonBinding
import com.arefdev.base.ui.custom.CustomTextView.CapitalizeType
import com.arefdev.base.utils.ResourceManager.getFont

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
open class RoundedButton : RoundedCornerLayout {

    private lateinit var binding: CustomRoundedButtonBinding

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CustomRoundedButtonBinding.inflate(LayoutInflater.from(context), this, true)

        isClickable = true

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedButton)

        setText(
            typedArray
                .getText(R.styleable.RoundedButton_android_text)
        )

        setTextColor(
            typedArray
                .getColor(R.styleable.RoundedButton_android_textColor, ContextCompat.getColor(context, R.color.colorPrimary))
        )

        setTextSize(
            typedArray
                .getDimension(R.styleable.RoundedButton_android_textSize, binding.tv.textSize)
        )

        if (typedArray.hasValue(R.styleable.RoundedButton_android_fontFamily)) {
            setTextTypeface(
                typedArray
                    .getString(R.styleable.RoundedButton_android_fontFamily)
            )
        }

        capitalize(
            typedArray
                .getInteger(R.styleable.RoundedButton_capitalize, 0)
        )

        typedArray.recycle()
    }

    fun setText(text: CharSequence?) {
        binding.tv.text = text
    }

    fun setTextColor(color: Int) {
        binding.tv.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        binding.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setTextTypeface(fontFamily: String?) {
        if (fontFamily == null) return
        if (isInEditMode)
            binding.tv.typeface = Typeface.create(fontFamily, Typeface.NORMAL)
        else
            binding.tv.typeface =
                getFont(context, fontFamily)
    }

    fun setTextTypeface(typeface: Typeface?) {
        binding.tv.typeface = typeface
    }

    fun capitalize(type: Int) {
        binding.tv.capitalize(type)
    }

    fun capitalize(type: CapitalizeType) {
        binding.tv.capitalize(type)
    }
}