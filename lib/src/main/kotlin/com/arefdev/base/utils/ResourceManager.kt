package com.arefdev.base.utils

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AnyRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import com.arefdev.base.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object ResourceManager {

    private var font: Typeface? = null
    private var fontBold: Typeface? = null

    /**
     * @return Regular font
     */
    fun getFont(context: Context): Typeface? {
        if (font == null)
            font = getTypeFace(context, R.font.font_family_default)
        return font
    }

    /**
     * @return Bold font
     */
    fun getFontBold(context: Context): Typeface? {
        if (fontBold == null) fontBold = getTypeFace(context, R.font.font_family_default_bold)
        return fontBold
    }

    fun getFont(context: Context, font: String): Typeface? =
        getTypeFace(context, font.substring(font.lastIndexOf("/") + 1))

    /**
     * Sets error to EditText and decorates its text using main font
     *
     * @param editText    EditText to set error
     * @param errorString Error text
     */
    fun createErrorSpan(editText: EditText, errorString: CharSequence?) {
        var ssbuilder: SpannableStringBuilder? = null
        if (!errorString.isNullOrEmpty()) {
            ssbuilder = SpannableStringBuilder(errorString)
            getFont(editText.context)?.let { typeface ->
                ssbuilder.setSpan(
                    CustomTypeFaceSpan(typeface),
                    0, ssbuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        if (editText is TextInputEditText) {
            try {
                (editText.getParent().parent as TextInputLayout).isErrorEnabled = ssbuilder != null
                (editText.getParent().parent as TextInputLayout).error = ssbuilder
            } catch (e: Exception) {
                editText.setError(ssbuilder)
            }
        } else {
            editText.error = ssbuilder
        }
    }

    /**
     * Sets "field is incorrect incorrect" error to EditText and decorates its text using main font
     *
     * @param editText EditText to set error
     */
    fun createIncorrectErrorSpan(editText: EditText) {
        val ssbuilder = SpannableStringBuilder(editText.hint.toString() + " صحیح نیست")
        getFont(editText.context)?.let { typeface ->
            ssbuilder.setSpan(
                CustomTypeFaceSpan(typeface),
                0, ssbuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (editText is TextInputEditText) {
            try {
                (editText.getParent().parent as TextInputLayout).error = ssbuilder
            } catch (e: Exception) {
                editText.setError(ssbuilder)
            }
        } else {
            editText.error = ssbuilder
        }
    }

    /**
     * Sets hint to TextView and decorates its text using main font
     *
     * @param textView   TextView to set error
     * @param hintString Hint text
     */
    fun createHintSpan(textView: TextView, hintString: CharSequence?) {
        var ssbuilder: SpannableStringBuilder? = null
        if (!hintString.isNullOrEmpty()) {
            ssbuilder = SpannableStringBuilder(hintString)
            getFont(textView.context)?.let { typeface ->
                ssbuilder.setSpan(
                    CustomTypeFaceSpan(typeface),
                    0, ssbuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        if (textView is TextInputEditText) {
            try {
                (textView.getParent().parent as TextInputLayout).hint = ssbuilder
            } catch (e: Exception) {
                textView.setHint(ssbuilder)
            }
        } else {
            textView.hint = ssbuilder
        }
    }

    fun getTypeFace(context: Context, familyName: String?): Typeface? {
        if (familyName != null) {
            val typeface: Typeface? = getTypeFace(
                context, context.resources.getIdentifier(
                    familyName.substring(
                        familyName.lastIndexOf("/") + 1,
                        familyName.lastIndexOf(".")
                    ),
                    "font",
                    context.packageName
                )
            )
            return typeface ?: Typeface.create(familyName, Typeface.NORMAL)
        }
        return null
    }

    fun getTypeFace(context: Context, @FontRes fontId: Int): Typeface? {
        return ResourcesCompat.getFont(context, fontId)
    }

    /**
     * Get uri to any resource type via Context Resource instance
     *
     * @param context Context
     * @param resId   Resource id
     * @return Uri to resource by given id
     * @throws Resources.NotFoundException if the given ID does not exist.
     */
    @Throws(Resources.NotFoundException::class)
    fun getUriToResource(context: Context, @AnyRes resId: Int): Uri {
        val res = context.resources
        return getUriToResource(res, resId)
    }

    /**
     * Get uri to any resource type via given Resource instance
     *
     * @param res   Resources instance
     * @param resId Resource id
     * @return Uri to resource by given id
     * @throws Resources.NotFoundException if the given ID does not exist.
     */
    @Throws(Resources.NotFoundException::class)
    fun getUriToResource(res: Resources, @AnyRes resId: Int): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + res.getResourcePackageName(resId)
                    + '/' + res.getResourceTypeName(resId)
                    + '/' + res.getResourceEntryName(resId)
        )
    }

    /**
     * Get uri to drawable resource
     *
     * @param context    Context
     * @param drawableId Drawable resource id
     * @return Uri to resource by given id
     * @throws Resources.NotFoundException if the given ID does not exist.
     */
    @Throws(Resources.NotFoundException::class)
    fun getUriToDrawable(context: Context, @DrawableRes drawableId: Int): Uri {
        return getUriToResource(context, drawableId)
    }
}
