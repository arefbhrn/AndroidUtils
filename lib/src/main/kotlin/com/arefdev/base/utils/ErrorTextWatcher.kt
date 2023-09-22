package com.arefdev.base.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class ErrorTextWatcher : TextWatcher {

    companion object {
        fun setOn(editText: EditText) {
            editText.addTextChangedListener(ErrorTextWatcher(editText))
        }

        fun setOn(textInputLayout: TextInputLayout) {
            textInputLayout.editText?.addTextChangedListener(ErrorTextWatcher(textInputLayout))
        }
    }

    private val editText: EditText?
    private val textInputLayout: TextInputLayout?

    constructor(editText: EditText) {
        this.editText = editText
        textInputLayout = null
    }

    constructor(textInputLayout: TextInputLayout) {
        editText = null
        this.textInputLayout = textInputLayout
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        editText?.error = null
        textInputLayout?.error = null
    }

    override fun afterTextChanged(s: Editable) {}
}
