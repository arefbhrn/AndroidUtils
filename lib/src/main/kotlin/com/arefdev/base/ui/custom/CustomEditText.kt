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
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import androidx.databinding.adapters.ListenerUtil
import com.arefdev.base.R
import com.arefdev.base.databinding.CommonCustomEditTextBinding
import com.arefdev.base.utils.KeyboardUtils.showKeyboard

/**
 * Updated on 30/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
@InverseBindingMethods(InverseBindingMethod(type = CustomEditText::class, attribute = "android:text"))
class CustomEditText : FrameLayout {

    companion object {

        @JvmStatic
        @BindingAdapter("android:textAttrChanged")
        fun setListener(view: CustomEditText, listener: InverseBindingListener) {
            val newValue = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    listener.onChange()
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            }
            val oldValue = ListenerUtil.trackListener(view, newValue, view.id)
            if (oldValue != null) {
                view.editText.removeTextChangedListener(oldValue)
            }
            view.editText.addTextChangedListener(newValue)
        }

        @JvmStatic
        @BindingAdapter("android:text")
        fun setTextValue(editText: CustomEditText, text: String?) {
            text?.let {
                if (it != editText.text) {
                    editText.text = it
                }
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text")
        fun getTextValue(editText: CustomEditText): String? {
            return editText.text
        }
    }

    private val STROKE_COLOR_DEFAULT = R.color.light_gray
    private val STROKE_COLOR_FOCUSED = R.color.colorPrimaryDark
    private val STROKE_COLOR_ERROR = R.color.red
    private lateinit var binding: CommonCustomEditTextBinding
    private var editable = true

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CommonCustomEditTextBinding.inflate(LayoutInflater.from(context), this, true)

        binding.lytParent.setOnClickListener {
            editText.requestFocus()
            editText.setSelection(editText.length())
            showKeyboard(editText)
        }

        editText.onFocusChangeListener =
            OnFocusChangeListener { _: View?, hasFocus: Boolean -> binding.lyt.setStrokeColor(getColor(if (hasFocus) STROKE_COLOR_FOCUSED else STROKE_COLOR_DEFAULT)) }

        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)

        setLabelText(
            typedArray.getText(R.styleable.CustomEditText_labelText)
        )

        if (typedArray.hasValue(R.styleable.CustomEditText_labelTextColor))
            setLabelTextColor(
                typedArray.getColor(R.styleable.CustomEditText_labelTextColor, Color.BLACK)
            )

        if (typedArray.hasValue(R.styleable.CustomEditText_labelTextSize))
            setLabelTextSize(
                typedArray.getDimension(
                    R.styleable.CustomEditText_labelTextSize,
                    context.resources.getDimension(R.dimen.text_size_small)
                )
            )
        if (typedArray.hasValue(R.styleable.CustomEditText_android_textColor))
            setTextColor(
                typedArray.getColor(R.styleable.CustomEditText_android_textColor, Color.BLACK)
            )

        if (typedArray.hasValue(R.styleable.CustomEditText_android_textSize))
            setTextSize(
                typedArray.getDimension(
                    R.styleable.CustomEditText_android_textSize,
                    context.resources.getDimension(R.dimen.text_size_default)
                )
            )

        if (typedArray.hasValue(R.styleable.CustomEditText_textBackground))
            setTextBackgroundTint(
                typedArray.getInt(R.styleable.CustomEditText_textBackground, Color.TRANSPARENT)
            )

        setHint(
            typedArray.getText(R.styleable.CustomEditText_android_hint)
        )

        if (typedArray.hasValue(R.styleable.CustomEditText_android_textColorHint))
            setHintTextColor(
                typedArray.getColor(R.styleable.CustomEditText_android_textColorHint, Color.LTGRAY)
            )

        if (typedArray.hasValue(R.styleable.CustomEditText_android_imeOptions))
            editText.imeOptions =
                typedArray.getInt(R.styleable.CustomEditText_android_imeOptions, EditorInfo.IME_ACTION_NONE)

        if (typedArray.hasValue(R.styleable.CustomEditText_android_inputType))
            editText.inputType =
                typedArray.getInt(R.styleable.CustomEditText_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL)

        if (typedArray.hasValue(R.styleable.CustomEditText_android_lines))
            editText.setLines(
                typedArray.getInt(R.styleable.CustomEditText_android_lines, 1)
            )

        editText.minLines = typedArray.getInt(R.styleable.CustomEditText_android_minLines, 1)

        editText.maxLines = typedArray.getInt(R.styleable.CustomEditText_android_maxLines, 1000000)

        if (typedArray.hasValue(R.styleable.CustomEditText_android_maxLength))
            editText.filters = arrayOf<InputFilter>(
                InputFilter.LengthFilter(
                    typedArray.getInt(R.styleable.CustomEditText_android_maxLength, -1)
                )
            )

        setText(typedArray.getText(R.styleable.CustomEditText_android_text))

        if (typedArray.hasValue(R.styleable.CustomEditText_android_enabled))
            isEnabled = typedArray.getBoolean(R.styleable.CustomEditText_android_enabled, true)

        if (typedArray.hasValue(R.styleable.CustomEditText_textGravity))
            setTextGravity(
                typedArray.getInt(R.styleable.CustomEditText_textGravity, Gravity.START)
            )

        setIcon(
            typedArray.getDrawable(R.styleable.CustomEditText_icon)
        )

        setError(
            typedArray.getText(R.styleable.CustomEditText_error)
        )

        setEditable(
            typedArray.getBoolean(R.styleable.CustomEditText_editable, true)
        )

        typedArray.recycle()

        editText.addTextChangedListener { setError(null) }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {}

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {}

    private fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    val pureText: CharSequence?
        get() = editText.text

    var text: String?
        get() = pureText.toString()
        set(text) {
            if (pureText == null && text != null || pureText != null && text == null || pureText != null && text != null && pureText.toString() != text.toString())
                editText.setText(text)
        }

    fun setText(text: CharSequence?) {
        this.text = text?.toString()
    }

    fun setTextColor(@ColorInt color: Int) {
        editText.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
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

    fun setLabelText(text: String?) {
        setLabelText(text as CharSequence?)
    }

    fun setLabelTextColor(@ColorInt color: Int) {
        binding.tvLabel.setTextColor(color)
    }

    fun setLabelTextSize(size: Float) {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setHint(text: CharSequence?) {
        editText.hint = text
    }

    fun setHint(text: String?) {
        setHint(text as CharSequence?)
    }

    fun setHintTextColor(@ColorInt color: Int) {
        editText.setHintTextColor(color)
    }

    val editText: EditText
        get() = binding.et

    fun setOnEditorActionListener(onEditorActionListener: TextView.OnEditorActionListener?) {
        editText.setOnEditorActionListener(onEditorActionListener)
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
        if (binding.tvError.text.contentEquals(error, true))
            return
        val errorEnabled = error.isNullOrEmpty().not()
        binding.tvError.text = error ?: ""
        binding.lytError.visibility =
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

    fun setError(error: String?) {
        setError(error as CharSequence?)
    }

    fun setEditable(editable: Boolean) {
        this.editable = editable
        editText.isEnabled = isEnabled && editable
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        setEditable(editable)
    }

    fun setTextGravity(gravity: Int) {
        editText.gravity = gravity
    }

    fun length(): Int {
        return editText.length()
    }

    val isEmpty: Boolean
        get() = editText.length() == 0

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        binding.lytParent.setOnClickListener(l)
        editText.setOnClickListener(l)
    }
}