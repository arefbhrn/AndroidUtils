package com.arefdev.base.ui.adapters.recyclerView.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

/**
 * Updated on 28/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class BaseListAdapter2<T, VDB : ViewDataBinding>(context: Context, list: List<T>) : BaseAdapter<ViewHolder<VDB>>(context) {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    interface OnItemRemovedListener<T> {
        fun onRemoved(t: T, position: Int)
    }

    val lifecycleOwner: LifecycleOwner? = if (context is LifecycleOwner) context else null
    private var recyclerView: RecyclerView? = null
    private var list: ObservableList<T> = ObservableArrayList()
    private val observableListCallback: OnListChangedCallback<ObservableList<T>>
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VDB

    init {
        observableListCallback = object : OnListChangedCallback<ObservableList<T>>() {
            override fun onChanged(sender: ObservableList<T>) {
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableList<T>, fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableList<T>, positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }
        }
        setList(list)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VDB> {
        val binding = bindingInflater.invoke(LayoutInflater.from(context), parent, false)
            .also { it.lifecycleOwner = this.lifecycleOwner }
        return ViewHolder(binding)
    }

    final override fun onBindingViewHolder(holder: ViewHolder<VDB>, position: Int) {
        onBindViewHolder(holder, list[position])
    }

    abstract fun onBindViewHolder(holder: ViewHolder<VDB>, item: T)

    fun setList(list: List<T>) {
        if (list is ObservableList<*>)
            this.list = list as ObservableList<T>
        else
            this.list = ObservableArrayList()

        this.list.removeOnListChangedCallback(observableListCallback)

        this.list.addOnListChangedCallback(observableListCallback)

        if (list is ObservableList<*>)
            notifyDataSetChanged()
        else
            this.list.addAll(list)
    }

    fun getList(): List<T> {
        return list
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItem(position: Int): T? {
        return if (position >= itemCount) null else list[position]
    }

    fun add(t: T) {
        list.add(t)
    }

    fun add(position: Int, t: T) {
        list.add(position, t)
    }

    fun addAll(newList: List<T>) {
        list.addAll(newList)
    }

    fun remove(position: Int) {
        list.removeAt(position)
        try {
            if (recyclerView != null) recyclerView!!.removeViewAt(position)
        } catch (ignored: Exception) {
        }
    }

    fun clear() {
        list.clear()
    }
}