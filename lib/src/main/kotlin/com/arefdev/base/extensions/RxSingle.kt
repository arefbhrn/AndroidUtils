package com.arefdev.base.extensions

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                    Single                                     |
// =================================================================================

fun <T : Any> Single<T>.subscribeVia(
    onError: (Throwable) -> Unit = {},
    finally: () -> Unit = {},
    onSuccess: (T) -> Unit = {}
): Disposable = doFinally(finally).subscribe(onSuccess, onError)

fun <T : Any> SingleEmitter<T>.onError() = onError(IllegalStateException())

fun <T : Any> Single<T>.subscribeOnIOThread(): Single<T> = subscribeOn(Schedulers.io())

fun <T : Any> Single<T>.subscribeOnSingleThread(): Single<T> = subscribeOn(Schedulers.single())

fun <T : Any> Single<T>.subscribeOnComputationThread(): Single<T> = subscribeOn(Schedulers.computation())

fun <T : Any> Single<T>.subscribeOnNewThread(): Single<T> = subscribeOn(Schedulers.newThread())

fun <T : Any> Single<T>.subscribeOnMainThread(): Single<T> = subscribeOn(AndroidSchedulers.mainThread())

fun <T : Any> Single<T>.observeOnSingleThread(): Single<T> = observeOn(Schedulers.single())

fun <T : Any> Single<T>.observeOnComputationThread(): Single<T> = observeOn(Schedulers.computation())

fun <T : Any> Single<T>.observeOnNewThread(): Single<T> = observeOn(Schedulers.newThread())

fun <T : Any> Single<T>.observeOnMainThread(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun <T : Any> Single<T>.blocking(defaultValue: T? = null, throwErrors: Boolean = false): T? {
    return try {
        blockingGet()
    } catch (e: Exception) {
        if (throwErrors)
            throw e
        else
            defaultValue
    }
}
