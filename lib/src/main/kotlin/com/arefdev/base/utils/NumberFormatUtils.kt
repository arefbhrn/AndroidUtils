package com.arefdev.base.utils

import android.annotation.SuppressLint
import java.text.DecimalFormat

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object NumberFormatUtils {
    @SuppressLint("DefaultLocale")
    fun digitDelimiter(number: Long): String {
        // The comma in the format specifier does the trick
        return String.format("%,d", number)
    }

    @SuppressLint("DefaultLocale")
    fun numberFormatter(number: Double, decimalPrecision: Int): String {
        return String.format("%,." + decimalPrecision + "f", number)
    }

    @SuppressLint("DefaultLocale")
    fun decimalFormatter(number: Double, decimalPrecision: Int): String {
        if (decimalPrecision == 0)
            return String.format("%,.0f", number)
        val precision = String.format("%0" + decimalPrecision + "d", 0)
        val formatter = DecimalFormat("#,###.$precision")
        return formatter.format(number)
    }
}
