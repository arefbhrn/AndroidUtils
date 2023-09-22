package com.arefdev.base.extensions

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.Subject

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                   Subject                                     |
// =================================================================================

fun <T : Any> Subject<T>.subscribeVia(
    onError: (Throwable) -> Unit = {},
    onComplete: () -> Unit = {},
    finally: () -> Unit = {},
    onNext: (T) -> Unit = {}
): Disposable = doFinally(finally).subscribe(onNext, onError, onComplete)

fun <T : Any> Subject<T>.subscribeOnIOThread(): Observable<T> = subscribeOn(Schedulers.io())

fun <T : Any> Subject<T>.subscribeOnSingleThread(): Observable<T> = subscribeOn(Schedulers.single())

fun <T : Any> Subject<T>.subscribeOnComputationThread(): Observable<T> = subscribeOn(Schedulers.computation())

fun <T : Any> Subject<T>.subscribeOnNewThread(): Observable<T> = subscribeOn(Schedulers.newThread())

fun <T : Any> Subject<T>.subscribeOnMainThread(): Observable<T> = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Subject<T>.observeOnSingleThread(): Observable<T> = observeOn(Schedulers.single())

fun <T : Any> Subject<T>.observeOnComputationThread(): Observable<T> = observeOn(Schedulers.computation())

fun <T : Any> Subject<T>.observeOnNewThread(): Observable<T> = observeOn(Schedulers.newThread())

fun <T : Any> Subject<T>.observeOnMainThread(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

@SuppressLint("CheckResult")
fun <T : Any> Subject<T>.blocking(throwErrors: Boolean = false) {
    try {
        blockingLast()
    } catch (e: Exception) {
        if (throwErrors)
            throw e
    }
}
