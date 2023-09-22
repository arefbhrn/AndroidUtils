package com.arefdev.base.model

import java.util.concurrent.TimeUnit

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
data class Time(
    val time: Long,
    val unit: TimeUnit
) {

    val millis: Long
        get() = unit.toMillis(time)

    val seconds: Long
        get() = unit.toSeconds(time)

    val minutes: Long
        get() = unit.toMinutes(time)

    val hours: Long
        get() = unit.toHours(time)

    val days: Long
        get() = unit.toDays(time)
}