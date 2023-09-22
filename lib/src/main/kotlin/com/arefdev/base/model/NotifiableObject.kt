package com.arefdev.base.model

import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
data class NotifiableObject<T : Any>(private var data: T) {

    private val publisher = BehaviorSubject.create<T>()

    fun observe(observer: Observer<T>) {
        publisher.subscribe(observer)
    }

    fun set(data: T) {
        this.data = data
        notifyChange()
    }

    fun notifyChange() {
        publisher.onNext(data)
    }
}
