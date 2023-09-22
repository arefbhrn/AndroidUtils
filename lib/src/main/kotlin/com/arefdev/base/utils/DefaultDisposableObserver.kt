package com.arefdev.base.utils

import io.reactivex.rxjava3.observers.DisposableObserver

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class DefaultDisposableObserver<T> : DisposableObserver<T>() {

    override fun onNext(t: T) {}

    override fun onError(e: Throwable) {}

    override fun onComplete() {}
}
