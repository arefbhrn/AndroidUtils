package com.arefdev.base.ui.adapters.recyclerView.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class BaseAdapter<VH : RecyclerView.ViewHolder>(@JvmField val context: Context) : RecyclerView.Adapter<VH>() {

    interface OnNextPageRequest<T> {
        fun onRequest(t: T)
    }

    enum class PaginationMode {
        PAGE_NUMBER,
        CURSOR
    }

    private var paginationMode: PaginationMode? = null
    private var hasNextPage = false
    private var nextPageNumber = 2
    private var nextPageCursor: String? = null
    private var onNextPageRequest: OnNextPageRequest<Any>? = null

    fun setPaginationMode(paginationMode: PaginationMode?) {
        this.paginationMode = paginationMode
    }

    fun setHasNextPage(hasNextPage: Boolean) {
        this.hasNextPage = hasNextPage
    }

    fun setNextPageCursor(nextPageCursor: String?) {
        this.nextPageCursor = nextPageCursor
    }

    fun setOnNextPageRequest(onNextPageRequest: OnNextPageRequest<Any>?) {
        this.onNextPageRequest = onNextPageRequest
    }

    fun resetNextPageNumber() {
        nextPageNumber = 2
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindingViewHolder(holder, position)
        if (!hasNextPage || holder.bindingAdapterPosition != itemCount - 1) return
        if (paginationMode == PaginationMode.PAGE_NUMBER) {
            if (onNextPageRequest != null) {
                onNextPageRequest!!.onRequest(nextPageNumber)
                nextPageNumber++
            }
        } else if (paginationMode == PaginationMode.CURSOR) {
            if (onNextPageRequest != null && nextPageCursor != null) {
                onNextPageRequest!!.onRequest(nextPageCursor!!)
            }
        }
    }

    abstract fun onBindingViewHolder(holder: VH, position: Int)
}
