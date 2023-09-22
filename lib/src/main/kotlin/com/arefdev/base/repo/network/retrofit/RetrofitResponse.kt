package com.arefdev.base.repo.network.retrofit

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Updated on 23/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
data class RetrofitResponse<T>(
    val data: T,
    val statusCode: Int
) : Single<RetrofitResponse<T>>() {

    override fun subscribeActual(observer: SingleObserver<in RetrofitResponse<T>>) {
        if (observer is Disposable && (observer as Disposable).isDisposed)
            return
        observer.onSuccess(this)
    }
}