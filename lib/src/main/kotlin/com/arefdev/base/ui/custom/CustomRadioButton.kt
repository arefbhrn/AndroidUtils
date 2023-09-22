package com.arefdev.base.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import com.arefdev.base.R
import com.arefdev.base.utils.generateColorStateList
import com.google.android.material.radiobutton.MaterialRadioButton

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class CustomRadioButton : MaterialRadioButton {

    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        CompoundButtonCompat.setButtonTintList(
            this,
            if (!isChecked) null else generateColorStateList(
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        )
        super.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            CompoundButtonCompat.setButtonTintList(
                buttonView,
                if (!isChecked) null else generateColorStateList(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
                )
            )

            onCheckedChangeListener?.onCheckedChanged(buttonView, isChecked)
        }
    }

    override fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        onCheckedChangeListener = listener
    }
}