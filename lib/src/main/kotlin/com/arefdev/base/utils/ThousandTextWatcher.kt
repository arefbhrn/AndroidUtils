package com.arefdev.base.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.StringTokenizer

/**
 * Updated on 27/07/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
open class ThousandTextWatcher(protected var editText: EditText) : TextWatcher {

    companion object {
        protected fun getDecimalFormattedString(value: String): String {
            val lst = StringTokenizer(value, ".")
            var str1 = value
            var str2 = ""
            if (lst.countTokens() > 1) {
                str1 = lst.nextToken()
                str2 = lst.nextToken()
            }
            var str3 = StringBuilder()
            var i = 0
            var j = -1 + str1.length
            if (str1[-1 + str1.length] == '.') {
                j--
                str3 = StringBuilder(".")
            }
            var k = j
            while (true) {
                if (k < 0) {
                    if (str2.isNotEmpty()) str3.append(".").append(str2)
                    return str3.toString()
                }
                if (i == 3) {
                    str3.insert(0, ",")
                    i = 0
                }
                str3.insert(0, str1[k])
                i++
                k--
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (editText.text.toString().length == 1 && editText.text.toString() == "0") editText.setText("")
    }

    override fun afterTextChanged(s: Editable) {
        editText.removeTextChangedListener(this)
        val value = editText.text.toString().trim()
        if (value.isNotEmpty()) {
            val str = editText.text.toString().replace(",".toRegex(), "")
            editText.setText(getDecimalFormattedString(str))
            editText.setSelection(editText.text.length)
        }
        editText.addTextChangedListener(this)
    }
}
