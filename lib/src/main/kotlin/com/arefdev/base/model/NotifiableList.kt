package com.arefdev.base.model

import androidx.annotation.RequiresApi
import com.arefdev.base.enums.SDK_CODES
import java.util.function.Predicate

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class NotifiableList<T : Any> : ArrayList<T>() {

    private val listeners = mutableListOf<Listener<T>>()

    interface Listener<T> {
        fun onAdd(index: Int)
        fun onAddAll(start: Int, end: Int)
        fun onChangeItem(index: Int, oldT: T, newT: T)
        fun onRemove(index: Int, t: T)
        fun onRemoveRange(start: Int, end: Int)
        fun onClear()
        fun onNotifyDatasetChanged()
    }

    class BaseListener<T> : Listener<T> {
        override fun onAdd(index: Int) {}
        override fun onAddAll(start: Int, end: Int) {}
        override fun onChangeItem(index: Int, oldT: T, newT: T) {}
        override fun onRemove(index: Int, t: T) {}
        override fun onRemoveRange(start: Int, end: Int) {}
        override fun onClear() {}
        override fun onNotifyDatasetChanged() {}
    }

    fun addListener(listener: Listener<T>) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener<T>) {
        listeners.remove(listener)
    }

    override fun add(element: T): Boolean {
        return super.add(element).also {
            if (!it) return@also
            for (listener in listeners) {
                listener.onAdd(size - 1)
            }
        }
    }

    override fun add(index: Int, element: T) {
        super.add(index, element).also {
            for (listener in listeners) {
                listener.onAdd(index)
            }
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return super.addAll(elements).also {
            for (listener in listeners) {
                listener.onAddAll(size - elements.size, size - 1)
            }
        }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return super.addAll(index, elements).also {
            for (listener in listeners) {
                listener.onAddAll(index, elements.size - 1)
            }
        }
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        if (index < 0)
            return false
        return removeAt(index) != null
    }

    override fun removeAt(index: Int): T {
        return super.removeAt(index).also {
            for (listener in listeners) {
                listener.onRemove(index, it)
            }
        }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return super.removeAll(elements).also {
            for (listener in listeners) {
                listener.onClear()
            }
        }
    }

    @RequiresApi(SDK_CODES.N)
    override fun removeIf(filter: Predicate<in T>): Boolean {
        for (index in indices) {
            if (filter.test(this[index]))
                removeAt(index)
        }
        return true
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex).also {
            for (listener in listeners) {
                listener.onRemoveRange(fromIndex, toIndex)
            }
        }
    }

    override fun set(index: Int, element: T): T {
        return super.set(index, element).also {
            for (listener in listeners) {
                listener.onChangeItem(index, it, element)
            }
        }
    }

    override fun clear() {
        super.clear().also {
            for (listener in listeners) {
                listener.onClear()
            }
        }
    }

    fun notifyDatasetChanged() {
        for (listener in listeners) {
            listener.onNotifyDatasetChanged()
        }
    }
}