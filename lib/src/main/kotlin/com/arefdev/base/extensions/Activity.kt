package com.arefdev.base.extensions

import android.app.Activity

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                   Activity                                    |
// =================================================================================

fun Activity.isOnMainThread(): Boolean {
    return com.arefdev.base.utils.isOnMainThread()
}
