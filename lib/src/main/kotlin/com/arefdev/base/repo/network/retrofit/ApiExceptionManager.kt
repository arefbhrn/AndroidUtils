package com.arefdev.base.repo.network.retrofit

import android.annotation.SuppressLint
import android.content.Context
import java.util.regex.Pattern

/**
 * Updated on 06/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object ApiExceptionManager {

    @SuppressLint("DiscouragedApi")
    fun getPersianMessage(context: Context, code: String): String? {
        val name = "net_ex_$code"
        return try {
            context.getString(context.resources.getIdentifier(name, "string", context.packageName))
        } catch (e: Exception) {
            null
        }
    }

    fun isPersian(str: String?): Boolean {
        if (str == null)
            return true
        val RTL_CHARACTERS = Pattern.compile("[\u0600-\u06FF\u0750-\u077F\u0590-\u05FF\uFE70-\uFEFF]")
        val matcher = RTL_CHARACTERS.matcher(str)
        return matcher.find()
    }
}