package com.arefdev.base.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import com.arefdev.base.R
import java.security.SecureRandom

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object StringUtils {

    private const val ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_+/="

    /**
     * Generates random string of given length from Base65 alphabet (numbers, lowercase letters, uppercase letters).
     *
     * @param length length
     * @return random string of given length
     */
    fun generateRandomString(length: Int): String {
        val secureRandom = SecureRandom()
        val sb = StringBuilder()
        for (i in 0 until length) {
            sb.append(ALPHABET[secureRandom.nextInt(ALPHABET.length)])
        }
        return sb.toString()
    }

    fun bold(str: CharSequence): SpannableString {
        val boldSpan = StyleSpan(Typeface.BOLD)
        val txtSpannable = SpannableString(str)
        txtSpannable.setSpan(boldSpan, 0, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return txtSpannable
    }

    fun bold(str: SpannableString): SpannableString {
        return bold(str as CharSequence)
    }

    fun bold(str: String): SpannableString {
        return bold(SpannableString(str))
    }

    fun strikethrough(str: CharSequence): SpannableString {
        val strikethroughSpan = StrikethroughSpan()
        val txtSpannable = SpannableString(str)
        txtSpannable.setSpan(strikethroughSpan, 0, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return txtSpannable
    }

    fun strikethrough(str: SpannableString): SpannableString {
        return strikethrough(str as CharSequence)
    }

    fun strikethrough(str: String): SpannableString {
        return strikethrough(SpannableString(str))
    }

    fun colorize(str: CharSequence, color: Int): SpannableString {
        val colorSpan = ForegroundColorSpan(color)
        val txtSpannable = SpannableString(str)
        txtSpannable.setSpan(colorSpan, 0, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return txtSpannable
    }

    fun colorize(str: SpannableString, color: Int): SpannableString {
        return colorize(str as CharSequence, color)
    }

    fun colorize(str: String, color: Int): SpannableString {
        return colorize(SpannableString(str), color)
    }

    fun getTimeAgo(context: Context, time: Long): String? {
        var mTime = time
        val SECOND_MILLIS = 1000
        val MINUTE_MILLIS = 60 * SECOND_MILLIS
        val HOUR_MILLIS = 60 * MINUTE_MILLIS
        val DAY_MILLIS = 24 * HOUR_MILLIS
        if (mTime < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            mTime *= 1000
        }
        val now = System.currentTimeMillis()
        if (mTime > now || mTime <= 0) {
            return null
        }
        val diff = now - mTime
        return if (diff < MINUTE_MILLIS) {
            context.getString(R.string.time_just_now)
        } else if (diff < 2 * MINUTE_MILLIS) {
            String.format(context.getString(R.string.time_minutes_ago), "1")
        } else if (diff < 50 * MINUTE_MILLIS) {
            String.format(context.getString(R.string.time_minutes_ago), (diff / MINUTE_MILLIS).toString())
        } else if (diff < 90 * MINUTE_MILLIS) {
            String.format(context.getString(R.string.time_hours_ago), "1")
        } else if (diff < 24 * HOUR_MILLIS) {
            String.format(context.getString(R.string.time_hours_ago), (diff / HOUR_MILLIS).toString())
        } else if (diff < 48 * HOUR_MILLIS) {
            context.getString(R.string.time_yesterday)
        } else {
            String.format(context.getString(R.string.time_days_ago), (diff / DAY_MILLIS).toString())
        }
    }

    fun charsToArabic(str: String?): String {
        if (str == null) return ""
        return str.replace("ی", "ي")
            .replace("ک", "ك")
    }

    fun charsToPersian(str: String?): String {
        if (str == null) return ""
        return str.replace("ي", "ی")
            .replace("ك", "ک")
    }

    fun strToArabic(str: String?): String {
        if (str == null) return ""
        return numToPersian(
            str.replace("ی", "ي")
                .replace("ک", "ك")
        )
    }

    fun strToPersian(str: String?): String {
        if (str == null) return ""
        return numToPersian(
            str.replace("ي", "ی")
                .replace("ك", "ک")
        )
    }

    fun numToPersian(str: String): String {
        return str.replace("0", "۰")
            .replace("1", "۱")
            .replace("2", "۲")
            .replace("3", "۳")
            .replace("4", "۴")
            .replace("5", "۵")
            .replace("6", "۶")
            .replace("7", "۷")
            .replace("8", "۸")
            .replace("9", "۹")
    }

    fun numToEnglish(str: String): String {
        return str.replace("۰", "0")
            .replace("۱", "1")
            .replace("۲", "2")
            .replace("۳", "3")
            .replace("۴", "4")
            .replace("۵", "5")
            .replace("۶", "6")
            .replace("۷", "7")
            .replace("۸", "8")
            .replace("۹", "9")
    }

    fun capitalizeWords(str: String): String {
        val words = str.split(" ").toTypedArray()
        val result = StringBuilder()
        for (word in words) {
            val capWord = if (word.length > 1)
                word[0].uppercase() + word.substring(1)
            else if (word.isNotEmpty())
                word[0].uppercase()
            else
                ""
            result.append(" ").append(capWord)
        }
        return result.substring(1)
    }

    fun unescapeJavaString(str: String): String {
        val sb = StringBuilder(str.length)
        var i = 0
        while (i < str.length) {
            var ch = str[i]
            if (ch == '\\') {
                val nextChar = if (i == str.length - 1) '\\' else str[i + 1]
                // Octal escape?
                if (nextChar in '0'..'7') {
                    var code = "" + nextChar
                    i++
                    if (i < str.length - 1 && str[i + 1] >= '0' && str[i + 1] <= '7') {
                        code += str[i + 1]
                        i++
                        if (i < str.length - 1 && str[i + 1] >= '0' && str[i + 1] <= '7') {
                            code += str[i + 1]
                            i++
                        }
                    }
                    sb.append(code.toInt(8).toChar())
                    i++
                    continue
                }
                when (nextChar) {
                    '\\' -> ch = '\\'
                    'b' -> ch = '\b'
                    'f' -> ch = '\u000c'
                    'n' -> ch = '\n'
                    'r' -> ch = '\r'
                    't' -> ch = '\t'
                    '\"' -> ch = '\"'
                    '\'' -> ch = '\''
                    'u' -> {
                        if (i >= str.length - 5) {
                            break
                        }
                        val code =
                            ("" + str[i + 2] + str[i + 3]
                                    + str[i + 4] + str[i + 5]).toInt(16)
                        sb.append(Character.toChars(code))
                        i += 5
                        i++
                        continue
                    }
                }
                i++
            }
            sb.append(ch)
            i++
        }
        return sb.toString()
    }
}
