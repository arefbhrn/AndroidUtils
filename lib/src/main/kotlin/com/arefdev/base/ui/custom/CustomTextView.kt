package com.arefdev.base.ui.custom

import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.CharacterStyle
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.arefdev.base.R
import com.arefdev.base.utils.StringUtils

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class CustomTextView : AppCompatTextView {

    enum class CapitalizeType {
        NONE, SENTENCES, WORDS, CHARACTERS
    }

    private var _text: CharSequence? = null
    private var capitalizeType = CapitalizeType.NONE.ordinal
    private var strikethrough = false
    private var isInternalFunction = false

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)

        text = typedArray.getText(R.styleable.CustomTextView_android_text) ?: ""

        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            typedArray.getDimension(R.styleable.CustomTextView_android_textSize, resources.getDimension(R.dimen.text_size_default))
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            letterSpacing = typedArray.getFloat(R.styleable.CustomTextView_android_letterSpacing, 0.03f)

        var singleLine: Boolean
        isSingleLine = typedArray.getBoolean(R.styleable.CustomTextView_android_singleLine, false).also { singleLine = it }

        ellipsize = TextUtils.TruncateAt.values()[
            typedArray.getInteger(R.styleable.CustomTextView_android_ellipsize, TextUtils.TruncateAt.MARQUEE.ordinal)
        ]

        marqueeRepeatLimit = typedArray.getInteger(R.styleable.CustomTextView_android_marqueeRepeatLimit, -1)

        isSelected = typedArray.getBoolean(R.styleable.CustomTextView_selected, singleLine)

        capitalize(typedArray.getInt(R.styleable.CustomTextView_capitalize, capitalizeType))

        setStrikethrough(typedArray.getBoolean(R.styleable.CustomTextView_strikethrough, strikethrough))

        typedArray.recycle()
    }

    fun capitalize(type: Int) {
        capitalize(
            if (type < 0 || type >= CapitalizeType.values().size)
                CapitalizeType.NONE
            else
                CapitalizeType.values()[type]
        )
    }

    fun capitalize(type: CapitalizeType) {
        capitalizeType = type.ordinal
        text = capitalizedText
    }

    private val capitalizedText: CharSequence?
        get() {
            val type = CapitalizeType.values()[capitalizeType]

            return if (_text == null) {
                null

            } else {
                when (type) {
                    CapitalizeType.NONE -> {
                        _text
                    }

                    CapitalizeType.SENTENCES -> {
                        isAllCaps = false
                        var result = SpannableStringBuilder()
                        var capitalize = true
                        var i = 0
                        while (i < _text!!.length) {
                            val c = _text!![i]
                            if (capitalize) {
                                result.append(Character.toUpperCase(c))
                                if (!Character.isWhitespace(c) && c != '.' && c != '\n') capitalize = false
                            } else {
                                result.append(c)
                                if (c == '.' || c == '\n') capitalize = true
                            }
                            i++
                        }
                        if (_text is Spanned) {
                            val restored = SpannableStringBuilder(result)
                            val spannedText = _text as Spanned
                            var nextSpanPos: Int
                            i = 0
                            while (i < spannedText.length) {
                                nextSpanPos = spannedText.nextSpanTransition(i, spannedText.length, CharacterStyle::class.java)
                                val spans = spannedText.getSpans(i, nextSpanPos, CharacterStyle::class.java)
                                for (span in spans) {
                                    restored.setSpan(span, i, nextSpanPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                }
                                i += nextSpanPos
                            }
                            result = restored
                        }
                        result
                    }

                    CapitalizeType.WORDS -> {
                        var result = SpannableStringBuilder()
                        var capitalize = true
                        var i = 0
                        while (i < _text!!.length) {
                            val c = _text!![i]
                            if (capitalize) {
                                result.append(Character.toUpperCase(c))
                                if (!Character.isWhitespace(c)) capitalize = false
                            } else {
                                result.append(c)
                                if (Character.isWhitespace(c)) capitalize = true
                            }
                            i++
                        }
                        if (_text is Spanned) {
                            val restored = SpannableStringBuilder(result)
                            val spannedText = _text as Spanned
                            var nextSpanPos: Int
                            i = 0
                            while (i < spannedText.length) {
                                nextSpanPos = spannedText.nextSpanTransition(i, spannedText.length, CharacterStyle::class.java)
                                val spans = spannedText.getSpans(i, nextSpanPos, CharacterStyle::class.java)
                                for (span in spans) {
                                    restored.setSpan(span, i, nextSpanPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                }
                                i += nextSpanPos
                            }
                            result = restored
                        }
                        result
                    }

                    CapitalizeType.CHARACTERS -> {
                        var result = SpannableStringBuilder()
                        var i = 0
                        while (i < _text!!.length) {
                            val c = _text!![i]
                            result.append(Character.toUpperCase(c))
                            i++
                        }
                        if (_text is Spanned) {
                            val restored = SpannableStringBuilder(result)
                            val spannedText = _text as Spanned
                            var nextSpanPos: Int
                            i = 0
                            while (i < spannedText.length) {
                                nextSpanPos = spannedText.nextSpanTransition(i, spannedText.length, CharacterStyle::class.java)
                                val spans = spannedText.getSpans(i, nextSpanPos, CharacterStyle::class.java)
                                for (span in spans) {
                                    restored.setSpan(span, i, nextSpanPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                                }
                                i += nextSpanPos
                            }
                            result = restored
                        }
                        result
                    }
                }
            }
        }

    override fun setText(text: CharSequence?, type: BufferType) {
        if (isInternalFunction) {
            isInternalFunction = false
            super.setText(text, type)

        } else {
            this._text = text
            update()
        }
    }

    fun setStrikethrough(strikethrough: Boolean) {
        this.strikethrough = strikethrough
        update()
    }

    private fun update() {
        isInternalFunction = true
        var mText = capitalizedText
        if (strikethrough) mText = StringUtils.strikethrough(mText ?: "")

        text = mText
    }
}