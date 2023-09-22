package com.arefdev.base.utils

import android.annotation.TargetApi
import android.content.Context
import com.arefdev.base.enums.SDK_CODES
import java.util.Locale

/**
 * Updated on 21/09/2023
 *
 * @author [Aref Bahreini](https://github.com/arefbhrn)
 */
object LocaleHelper {

    // the method is used to set the language at runtime
    fun setLocale(context: Context, language: String): Context {
        return if (isOsAtLeast(SDK_CODES.N))
            updateResources(context, language)
        else
            updateResourcesLegacy(context, language)
    }

    // the method is used update the language of application by creating
    // object of inbuilt Locale class and passing language argument to it
    @TargetApi(SDK_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (isOsAtLeast(SDK_CODES.JELLY_BEAN_MR1)) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}
