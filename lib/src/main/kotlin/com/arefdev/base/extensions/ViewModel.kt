package com.arefdev.base.extensions

import androidx.lifecycle.ViewModel

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                  ViewModel                                    |
// =================================================================================

fun ViewModel.isOnMainThread(): Boolean {
    return com.arefdev.base.utils.isOnMainThread()
}
