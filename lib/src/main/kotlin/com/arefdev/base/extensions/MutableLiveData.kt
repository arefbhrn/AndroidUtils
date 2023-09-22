package com.arefdev.base.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.util.Util.isOnMainThread

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                MutableLiveData                                |
// =================================================================================

fun <T> MutableLiveData<T>.sendValue(value: T?) {
    if (isOnMainThread())
        this.value = value
    else
        this.postValue(value)
}

fun <T> MutableLiveData<T>.notify() {
    this.sendValue(value)
}

fun <T> LiveData<T>.asMutable(): MutableLiveData<T>? {
    return if (this is MutableLiveData) this else null
}
