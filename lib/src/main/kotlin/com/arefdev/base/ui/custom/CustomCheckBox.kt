package com.arefdev.base.ui.custom

import android.content.Context
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomCheckBoxBinding

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class CustomCheckBox : LinearLayout {

    interface OnCheckChangedListener {
        fun onCheckChanged(isChecked: Boolean)
    }

    private lateinit var binding: CustomCheckBoxBinding
    private var onCheckChangedListener: OnCheckChangedListener? = null
    private var isChecked = false

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CustomCheckBoxBinding.inflate(LayoutInflater.from(context), this, true)

        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomCheckBox)

        setText(
            styledAttrs
                .getText(R.styleable.CustomCheckBox_android_text)
        )

        setChecked(
            styledAttrs
                .getBoolean(R.styleable.CustomCheckBox_android_checked, false)
        )

        if (styledAttrs.hasValue(R.styleable.CustomCheckBox_android_textColorLink))
            setLinkTextColor(
                styledAttrs.getColor(
                    R.styleable.CustomCheckBox_android_textColorLink,
                    ContextCompat.getColor(context, R.color.colorPrimary)
                )
            )

        styledAttrs.recycle()
    }

    fun setText(text: CharSequence?) {
        binding.tv.text = text
    }

    fun setChecked(checked: Boolean) {
        isChecked = checked
        binding.btn.visibility = if (checked) VISIBLE else GONE
        if (onCheckChangedListener != null) onCheckChangedListener!!.onCheckChanged(isChecked)
    }

    fun toggle() {
        setChecked(!isChecked)
    }

    fun isChecked(): Boolean {
        return isChecked
    }

    fun setMovementMethod(movement: MovementMethod?) {
        binding.tv.movementMethod = movement
    }

    fun setLinkTextColor(color: Int) {
        binding.tv.setLinkTextColor(color)
    }

    fun setOnCheckChangedListener(onCheckChangedListener: OnCheckChangedListener?) {
        this.onCheckChangedListener = onCheckChangedListener
    }
}