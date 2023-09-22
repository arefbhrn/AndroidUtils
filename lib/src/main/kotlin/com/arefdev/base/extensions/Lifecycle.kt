package com.arefdev.base.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                   Lifecycle                                   |
// =================================================================================

fun Lifecycle.addObserver(observer: (event: Lifecycle.Event) -> Unit) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                observer(event)
            }
        }
    })
}

fun Lifecycle.setOneTimeEvent(event: Lifecycle.Event, unit: () -> Unit) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, mEvent: Lifecycle.Event) {
            if (mEvent == event) {
                unit()
                removeObserver(this)
            }
        }
    })
}