package com.arefdev.base.utils

import java.util.regex.Pattern

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object ValidationUtils {

    /**
     * Password must be between 6 and 20 characters.
     */
    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    /**
     * Password must contains lowercase, UPPERCASE, number and special characters. It must be
     * between 8 and 20 characters.
     */
    fun isComplexPassword(password: String): Boolean {
        val pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})")
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    /**
     * Password must contains lowercase, UPPERCASE, number and special characters. It must be
     * more than 8.
     *
     * @return integer between 0 to 10
     */
    fun getPasswordStrength(password: String): Int {
        var iPasswordScore = 0
        iPasswordScore += if (password.length < 8) return 0 else if (password.length >= 10) 2 else 1

        //if it contains one digit, add 2 to total score
        if (password.matches("(?=.*[0-9]).*".toRegex())) iPasswordScore += 2

        //if it contains one lower case letter, add 2 to total score
        if (password.matches("(?=.*[a-z]).*".toRegex())) iPasswordScore += 2

        //if it contains one upper case letter, add 2 to total score
        if (password.matches("(?=.*[A-Z]).*".toRegex())) iPasswordScore += 2

        //if it contains one special character, add 2 to total score
        if (password.matches("(?=.*[~!@#$%^&*()_-]).*".toRegex())) iPasswordScore += 2
        return iPasswordScore
    }

    fun isEmailValid(emailAddress: String): Boolean {
        val pattern = Pattern.compile(
            "^[a-z-0-9._]+@([a-z-0-9]+(\\.[a-z])*)+\\.[a-z]{2,4}$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(emailAddress)
        return matcher.matches()
    }

    /**
     * Mobile number must start with "09" or "9" and followed by 9 numbers.
     */
    fun isMobileNumberValid(mobileNumber: String): Boolean {
        val pattern = Pattern.compile("^0?9[0-9]{2}[0-9]{7}$")
        val matcher = pattern.matcher(mobileNumber)
        return matcher.matches()
    }
}
