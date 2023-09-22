package com.arefdev.base.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.databinding.adapters.ListenerUtil
import androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged
import androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged
import androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomTitledEditTextBinding
import com.arefdev.base.utils.KeyboardUtils.showKeyboard

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
@InverseBindingMethods(InverseBindingMethod(type = TitledEditText::class, attribute = "android:text"))
class TitledEditText : FrameLayout {

    companion object {

        @JvmStatic
        @BindingAdapter(value = ["android:beforeTextChanged", "android:onTextChanged", "android:afterTextChanged", "android:textAttrChanged"], requireAll = false)
        fun setListener(view: TitledEditText, before: BeforeTextChanged?,
                        on: OnTextChanged?, after: AfterTextChanged?,
                        textAttrChanged: InverseBindingListener?) {
            val newValue: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    before?.beforeTextChanged(s, start, count, after)
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    on?.onTextChanged(s, start, before, count)
                    textAttrChanged?.onChange()
                    view.setError(null)
                }

                override fun afterTextChanged(s: Editable) {
                    after?.afterTextChanged(s)
                }
            }
            val oldValue = ListenerUtil.trackListener(view, newValue, view.id)
            if (oldValue != null) {
                view.editText.removeTextChangedListener(oldValue)
            }
            view.editText.addTextChangedListener(newValue)
        }

        @JvmStatic
        @BindingAdapter("android:text")
        fun setText(editText: TitledEditText, text: String?) {
            text?.let {
                if (it != editText.text) {
                    editText.text = it
                }
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun getText(editText: TitledEditText): String? {
            return editText.text?.toString()
        }
    }

    private val STROKE_COLOR_DEFAULT = R.color.light_gray
    private val STROKE_COLOR_FOCUSED = R.color.colorPrimaryDark
    private val STROKE_COLOR_ERROR = R.color.red
    private lateinit var binding: CustomTitledEditTextBinding
    private var editable = true

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CustomTitledEditTextBinding.inflate(LayoutInflater.from(context), this, true)

        binding.lytParent.setOnClickListener {
            binding.et.requestFocus()
            binding.et.setSelection(binding.et.length())
            showKeyboard(binding.et)
        }

        binding.et.onFocusChangeListener =
            OnFocusChangeListener { _: View?, hasFocus: Boolean -> binding.lyt.setStrokeColor(getColor(if (hasFocus) STROKE_COLOR_FOCUSED else STROKE_COLOR_DEFAULT)) }

        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitledEditText)

        setLabelText(
            typedArray.getText(R.styleable.TitledEditText_labelText)
        )

        if (typedArray.hasValue(R.styleable.TitledEditText_labelTextColor))
            setLabelTextColor(
                typedArray.getColor(R.styleable.TitledEditText_labelTextColor, Color.BLACK)
            )

        if (typedArray.hasValue(R.styleable.TitledEditText_labelTextSize))
            setLabelTextSize(
                typedArray.getDimension(R.styleable.TitledEditText_labelTextSize, context.resources.getDimension(R.dimen.text_size_small))
            )
        if (typedArray.hasValue(R.styleable.TitledEditText_android_textColor))
            setTextColor(
                typedArray.getColor(R.styleable.TitledEditText_android_textColor, Color.BLACK)
            )

        if (typedArray.hasValue(R.styleable.TitledEditText_android_textSize))
            setTextSize(
                typedArray.getDimension(R.styleable.TitledEditText_android_textSize, context.resources.getDimension(R.dimen.text_size_default))
            )

        if (typedArray.hasValue(R.styleable.TitledEditText_textBackground))
            setTextBackgroundTint(
                typedArray.getInt(R.styleable.TitledEditText_textBackground, Color.TRANSPARENT)
            )

        setHint(
            typedArray.getText(R.styleable.TitledEditText_android_hint)
        )

        if (typedArray.hasValue(R.styleable.TitledEditText_android_textColorHint))
            setHintTextColor(
                typedArray.getColor(R.styleable.TitledEditText_android_textColorHint, Color.LTGRAY)
            )

        if (typedArray.hasValue(R.styleable.TitledEditText_android_imeOptions))
            binding.et.imeOptions = typedArray.getInt(R.styleable.TitledEditText_android_imeOptions, EditorInfo.IME_ACTION_NONE)

        if (typedArray.hasValue(R.styleable.TitledEditText_android_inputType))
            binding.et.inputType = typedArray.getInt(R.styleable.TitledEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)

        if (typedArray.hasValue(R.styleable.TitledEditText_android_lines))
            binding.et.setLines(
                typedArray.getInt(R.styleable.TitledEditText_android_lines, 1)
            )

        binding.et.minLines = typedArray.getInt(R.styleable.TitledEditText_android_minLines, 1)

        binding.et.maxLines = typedArray.getInt(R.styleable.TitledEditText_android_maxLines, 1000000)

        if (typedArray.hasValue(R.styleable.TitledEditText_android_maxLength))
            binding.et.filters = arrayOf<InputFilter>(
                InputFilter.LengthFilter(
                    typedArray.getInt(R.styleable.TitledEditText_android_maxLength, -1)
                )
            )

        text = typedArray.getText(R.styleable.TitledEditText_android_text)

        if (typedArray.hasValue(R.styleable.TitledEditText_android_enabled))
            isEnabled = typedArray.getBoolean(R.styleable.TitledEditText_android_enabled, true)

        if (typedArray.hasValue(R.styleable.TitledEditText_textGravity))
            setTextGravity(
                typedArray.getInt(R.styleable.TitledEditText_textGravity, Gravity.START)
            )

        setIcon(
            typedArray.getDrawable(R.styleable.TitledEditText_icon)
        )

        setError(
            typedArray.getText(R.styleable.TitledEditText_error)
        )

        setEditable(
            typedArray.getBoolean(R.styleable.TitledEditText_editable, true)
        )

        typedArray.recycle()
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {}

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {}

    private fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    val pureText: CharSequence?
        get() = binding.et.text

    var text: CharSequence?
        get() = pureText
        set(text) {
            if (pureText == null && text != null || pureText != null && text == null || pureText != null && text != null && pureText.toString() != text.toString())
                binding.et.setText(text)
        }

    fun setTextColor(@ColorInt color: Int) {
        binding.et.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        binding.et.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
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
        binding.et.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setHint(text: CharSequence?) {
        binding.et.hint = text
    }

    fun setHintTextColor(@ColorInt color: Int) {
        binding.et.setHintTextColor(color)
    }

    val editText: EditText
        get() = binding.et

    fun setOnEditorActionListener(onEditorActionListener: TextView.OnEditorActionListener?) {
        binding.et.setOnEditorActionListener(onEditorActionListener)
    }

    fun setTextBackgroundTint(color: Int) {
        binding.lyt.setBackgroundColor(color)
    }

    fun setIcon(@DrawableRes resId: Int) {
        binding.icon.setImageResource(resId)
    }

    fun setIcon(drawable: Drawable?) {
        binding.icon.setImageDrawable(drawable)
        binding.icon.visibility = if (drawable == null) GONE else VISIBLE
    }

    fun setError(error: CharSequence?) {
        val errorEnabled = error.isNullOrEmpty().not()
        binding.tvError.text = error ?: ""
        binding.tvError.visibility =
            if (errorEnabled && error?.toString()?.trim { it <= ' ' }.isNullOrEmpty().not())
                VISIBLE
            else
                GONE
        binding.lyt.setStrokeColor(
            getColor(
                if (errorEnabled)
                    STROKE_COLOR_ERROR
                else if (hasFocus())
                    STROKE_COLOR_FOCUSED
                else
                    STROKE_COLOR_DEFAULT
            )
        )
    }

    fun setEditable(editable: Boolean) {
        this.editable = editable
        binding.et.isEnabled = isEnabled && editable
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        setEditable(editable)
    }

    fun setTextGravity(gravity: Int) {
        binding.et.gravity = gravity
    }

    fun length(): Int {
        return binding.et.length()
    }

    val isEmpty: Boolean
        get() = binding.et.length() == 0

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        binding.lytParent.setOnClickListener(l)
        binding.et.setOnClickListener(l)
    }
}