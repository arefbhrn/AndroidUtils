package com.arefdev.base.utils

import io.reactivex.rxjava3.observers.DisposableSingleObserver

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class DefaultDisposableSingleObserver<T> : DisposableSingleObserver<T>() {

    override fun onSuccess(t: T) {}

    override fun onError(e: Throwable) {}
}
