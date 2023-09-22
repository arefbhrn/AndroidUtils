package com.arefdev.base.extensions

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                  Observable                                   |
// =================================================================================

fun <T : Any> Observable<T>.subscribeVia(
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {},
    finally: () -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable = doFinally(finally).subscribe(onNext, onError, onComplete)

fun <T : Any> Observable<T>.subscribeOnIOThread(): Observable<T> = subscribeOn(Schedulers.io())

fun <T : Any> Observable<T>.subscribeOnSingleThread(): Observable<T> = subscribeOn(Schedulers.single())

fun <T : Any> Observable<T>.subscribeOnComputationThread(): Observable<T> = subscribeOn(Schedulers.computation())

fun <T : Any> Observable<T>.subscribeOnNewThread(): Observable<T> = subscribeOn(Schedulers.newThread())

fun <T : Any> Observable<T>.subscribeOnMainThread(): Observable<T> = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Observable<T>.observeOnSingleThread(): Observable<T> = observeOn(Schedulers.single())

fun <T : Any> Observable<T>.observeOnComputationThread(): Observable<T> = observeOn(Schedulers.computation())

fun <T : Any> Observable<T>.observeOnNewThread(): Observable<T> = observeOn(Schedulers.newThread())

fun <T : Any> Observable<T>.observeOnMainThread(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

fun <T : Any> Observable<T>.blocking(defaultValue: T? = null, throwErrors: Boolean = false): T? {
    return try {
        blockingFirst()
    } catch (e: Exception) {
        if (throwErrors)
            throw e
        else
            defaultValue
    }
}
