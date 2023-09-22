package com.arefdev.base.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.arefdev.base.extensions.observeOnMainThread
import com.arefdev.base.extensions.sendValue
import com.arefdev.base.extensions.subscribeVia
import com.arefdev.base.repo.network.retrofit.RetrofitRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Updated on 09/09/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class LifecycleAwareView<VDB : ViewDataBinding> : View, LifecycleOwner {

    val binding: VDB by lazy { bindingInflater.invoke(LayoutInflater.from(context)).also { it.lifecycleOwner = this } }
    protected abstract val bindingInflater: (LayoutInflater) -> VDB
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val isLoading = MutableLiveData(false)
    private val onClearDisposables = CompositeDisposable()
    private val onClearObservers: MutableMap<LiveData<*>, MutableList<Observer<*>>> = HashMap()
    private val onClearRetrofitRequests: MutableList<RetrofitRequest<*>> = mutableListOf()

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    fun startLoading() {
        Observable.just(true).observeOnMainThread().subscribeVia { isLoading.sendValue(it) }
    }

    fun stopLoading() {
        Observable.just(false).observeOnMainThread().subscribeVia { isLoading.sendValue(it) }
    }

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun addOnClearDisposable(disposable: Disposable?) {
        if (disposable == null) return
        onClearDisposables.add(disposable)
    }

    fun <T> addOnClearDisposable(liveData: LiveData<T>, observer: Observer<T>?) {
        if (observer == null)
            return

        var observers = onClearObservers[liveData]
        if (observers == null)
            observers = mutableListOf()

        observers.add(observer)
        onClearObservers[liveData] = observers

        liveData.observeForever(observer)
    }

    fun <T> addOnClearDisposable(request: RetrofitRequest<T>?) {
        if (request == null) return
        onClearRetrofitRequests.add(request)
    }

    fun removeOnClearDisposable(disposable: Disposable) {
        onClearDisposables.remove(disposable)
    }
}