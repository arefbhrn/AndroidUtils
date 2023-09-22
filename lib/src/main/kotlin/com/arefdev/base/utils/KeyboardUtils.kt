package com.arefdev.base.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object KeyboardUtils {
    /**
     * Shows soft keyboard
     *
     * @param view The currently focused view
     */
    fun showKeyboard(view: View?) {
        if (view == null) return
        view.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    /**
     * Hides soft keyboard
     */
    fun hideKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
    /**
     * Hides soft keyboard
     */
    fun hideKeyboard(activity: Activity?) {
        if (activity == null || activity.currentFocus == null) return
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }
}
