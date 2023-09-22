package com.arefdev.base.extensions

import androidx.fragment.app.Fragment

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                   Fragment                                    |
// =================================================================================

fun Fragment.isOnMainThread(): Boolean {
    return com.arefdev.base.utils.isOnMainThread()
}
