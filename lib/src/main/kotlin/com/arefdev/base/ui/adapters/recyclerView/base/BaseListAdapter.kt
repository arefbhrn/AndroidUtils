package com.arefdev.base.ui.adapters.recyclerView.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.arefdev.base.extensions.sendValue

/**
 * Updated on 28/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class BaseListAdapter<T, VDB : ViewDataBinding>(context: Context, list: List<T>) : BaseAdapter<ViewHolder<VDB>>(context) {

    interface OnItemClickListener {
        fun onClick(position: Int)
    }

    private val lifecycleOwner: LifecycleOwner? = if (context is LifecycleOwner) context else null
    private var recyclerView: RecyclerView? = null
    private var list: MutableList<T> = mutableListOf()
    private val listLiveData = MutableLiveData<List<T>>()
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> VDB

    init {
        this.list.addAll(list)
        listLiveData.sendValue(this.list)
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

    override fun onBindingViewHolder(holder: ViewHolder<VDB>, position: Int) {
        onBindViewHolder(holder, list[position])
    }

    abstract fun onBindViewHolder(holder: ViewHolder<VDB>, item: T)

    open fun setList(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
        listLiveData.sendValue(this.list)
        notifyDataSetChanged()
    }

    fun getList(): List<T> {
        return list
    }

    fun getListLiveData(): LiveData<List<T>> {
        return listLiveData
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItem(position: Int): T? {
        return if (position >= itemCount) null else list[position]
    }

    fun add(t: T) {
        list.add(t)
        listLiveData.sendValue(list)
        notifyItemInserted(itemCount - 1)
    }

    fun add(position: Int, t: T) {
        list.add(position, t)
        listLiveData.sendValue(list)
        notifyItemInserted(position)
    }

    fun <M : T> addAll(newList: List<M>) {
        val lastItemCount = itemCount
        list.addAll(newList as Collection<T>)
        listLiveData.sendValue(list)
        notifyItemRangeInserted(lastItemCount, newList.size)
    }

    open fun remove(position: Int) {
        list.removeAt(position)
        listLiveData.sendValue(list)
        try {
            if (recyclerView != null) recyclerView!!.removeViewAt(position)
        } catch (ignored: Exception) {
        }
        notifyItemRemoved(position)
    }

    fun clear() {
        list.clear()
        listLiveData.sendValue(list)
        notifyDataSetChanged()
    }
}