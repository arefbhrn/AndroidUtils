package com.arefdev.base.extensions

import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                   Exception                                   |
// =================================================================================

fun Throwable.isTimeout(): Boolean {
    if (this is TimeoutException)
        return true

    val messages = mutableListOf<String>().apply {
        add("java.net.SocketException: Connection reset")
        add("java.io.InterruptedIOException: timeout")
    }

    for (errMessage in messages)
        if (toString().equals(errMessage, ignoreCase = true)
            || cause.toString().equals(errMessage, ignoreCase = true)
        )
            return true

    return false
}

fun Throwable.isSSLHandshakeError(): Boolean {
    var e: Throwable? = this
    while (e != null) {
        if (e is SSLHandshakeException)
            return true
        e = e.cause
    }
    return false
}
