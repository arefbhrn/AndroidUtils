package com.arefdev.base.ui.custom

import android.content.Context
import android.util.AttributeSet
import com.arefdev.base.R
import com.google.android.material.textfield.TextInputEditText

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class CustomTextInputEditText : TextInputEditText {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomTextInputEditText)
        setLines(
            styledAttrs
                .getInteger(R.styleable.CustomTextInputEditText_android_lines, 1)
        )
        maxLines = styledAttrs
            .getInteger(R.styleable.CustomTextInputEditText_android_maxLines, 1)
        styledAttrs.recycle()
    }

    val isEmpty: Boolean
        get() = length() == 0
}