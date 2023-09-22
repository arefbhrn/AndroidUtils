package com.arefdev.base.ui.adapters.recyclerView.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.arefdev.base.extensions.sendValue

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class ViewModel<T, VDB : ViewDataBinding>(data: T, holder: ViewHolder<VDB>) : ViewModel() {

    private val data = MutableLiveData<T>()
    val holder: ViewHolder<VDB>
    private var observer: Observer<T>? = null

    fun getData(): T? {
        return data.value
    }

    fun setData(data: T) {
        this.data.sendValue(data)
    }

    fun setObserver(owner: LifecycleOwner?, observer: Observer<T>?) {
        this.observer = observer
        data.observe(owner!!, observer!!)
    }

    fun setObserverForever(observer: Observer<T>?) {
        this.observer = observer
        data.observeForever(observer!!)
    }

    fun removeObserver() {
        data.removeObserver(observer!!)
    }

    init {
        this.data.sendValue(data)
        this.holder = holder
    }
}