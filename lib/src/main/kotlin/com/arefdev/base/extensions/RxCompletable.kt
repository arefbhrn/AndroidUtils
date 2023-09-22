package com.arefdev.base.extensions

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

///**
// * Updated on 21/09/2023
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

// =================================================================================
// |                                 Completable                                   |
// =================================================================================

fun Completable.subscribeVia(
    onError: (Throwable) -> Unit = {},
    finally: () -> Unit = {},
    onComplete: () -> Unit = {}
): Disposable = doFinally(finally).subscribe(onComplete, onError)

fun CompletableEmitter.onError() = onError(IllegalStateException())

fun Completable.subscribeOnIOThread(): Completable = subscribeOn(Schedulers.io())

fun Completable.subscribeOnSingleThread(): Completable = subscribeOn(Schedulers.single())

fun Completable.subscribeOnComputationThread(): Completable = subscribeOn(Schedulers.computation())

fun Completable.subscribeOnNewThread(): Completable = subscribeOn(Schedulers.newThread())

fun Completable.subscribeOnMainThread(): Completable = subscribeOn(AndroidSchedulers.mainThread())

fun Completable.observeOnSingleThread(): Completable = observeOn(Schedulers.single())

fun Completable.observeOnComputationThread(): Completable = observeOn(Schedulers.computation())

fun Completable.observeOnNewThread(): Completable = observeOn(Schedulers.newThread())

fun Completable.observeOnMainThread(): Completable = observeOn(AndroidSchedulers.mainThread())

@SuppressLint("CheckResult")
fun Completable.blocking(throwErrors: Boolean = false) {
    try {
        blockingAwait()
    } catch (e: Exception) {
        if (throwErrors)
            throw e
    }
}
