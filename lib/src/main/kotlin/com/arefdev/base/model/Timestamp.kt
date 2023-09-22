package com.arefdev.base.model

import com.arefdev.base.utils.CalendarTool
import java.io.Serializable

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
data class Timestamp(val millis: Long) : Comparable<Timestamp>, Serializable {

    constructor() : this(CalendarTool.now().timeInMillis)

    /**
     * @param str Formatted in "yyyy-MM-dd'T'HH:mm:ssZ"
     */
    constructor(str: String) : this(CalendarTool(str).timeInMillis)

    val calendar: CalendarTool
        get() = CalendarTool(millis)

    override fun compareTo(other: Timestamp): Int {
        return millis.compareTo(other.millis)
    }

    override fun toString(): String {
        return millis.toString()
    }
}