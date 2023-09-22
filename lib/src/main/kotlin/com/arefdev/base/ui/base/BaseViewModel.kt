package com.arefdev.base.ui.base

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.arefdev.base.extensions.sendValue
import com.arefdev.base.utils.runOnMainThread
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Updated on 28/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
open class BaseViewModel : ViewModel() {

    val isLoading = MutableLiveData(false)
    private val onClearDisposables = CompositeDisposable()
    private val onClearObservers: MutableMap<LiveData<*>, MutableList<Observer<*>>> = HashMap()

    fun startLoading() {
        isLoading.sendValue(true)
    }

    fun stopLoading() {
        isLoading.sendValue(false)
    }

    fun addOnClearDisposable(disposable: Disposable?) {
        if (disposable == null) return
        onClearDisposables.add(disposable)
    }

    fun <T> addOnClearDisposable(liveData: LiveData<T>, observer: Observer<T>?) {
        if (observer == null)
            return

        val observers = onClearObservers[liveData] ?: mutableListOf()
        observers.add(observer)
        onClearObservers[liveData] = observers

        runOnMainThread { liveData.observeForever(observer) }
    }

    fun removeOnClearDisposable(disposable: Disposable) {
        onClearDisposables.remove(disposable)
    }

    fun convertDpToPx(dp: Float): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onCleared() {
        super.onCleared()

        onClearDisposables.dispose()

        for (liveData in onClearObservers.keys)
            for (observer in onClearObservers[liveData]!!)
                liveData.removeObserver(observer as Observer<Any>)
    }
}