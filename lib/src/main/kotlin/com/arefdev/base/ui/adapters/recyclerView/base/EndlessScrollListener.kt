package com.arefdev.base.ui.adapters.recyclerView.base

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Updated on 28/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class EndlessScrollListener : RecyclerView.OnScrollListener {

    companion object {
        fun set(recyclerView: RecyclerView, onLoadMore: (view: View?, page: Int, totalItemsCount: Int) -> Unit) {
            recyclerView.addOnScrollListener(object : EndlessScrollListener(recyclerView) {
                override fun onLoadMore(view: View?, page: Int, totalItemsCount: Int) {
                    onLoadMore.invoke(view, page, totalItemsCount)
                }
            })
        }
    }

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 2
    // The current offset index of data you have loaded
    private var currentPage = 1
    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0
    // True if we are still waiting for the last set of data to load.
    private var loading = true
    // Sets the starting page index
    private var startingPageIndex = 1
    private val mLayoutManager: RecyclerView.LayoutManager

    constructor(recyclerView: RecyclerView) {
        mLayoutManager = recyclerView.layoutManager!!
    }

    constructor(layoutManager: RecyclerView.LayoutManager) {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: RecyclerView.LayoutManager, startingPageIndex: Int) {
        mLayoutManager = layoutManager
        this.startingPageIndex = startingPageIndex
        currentPage = startingPageIndex
    }

    fun reset() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
    }

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount
        if (mLayoutManager is GridLayoutManager) lastVisibleItemPosition =
            mLayoutManager.findLastVisibleItemPosition() else if (mLayoutManager is LinearLayoutManager) lastVisibleItemPosition =
            mLayoutManager.findLastVisibleItemPosition()

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(view, currentPage, totalItemCount)
            loading = true
        }
        if (view.computeVerticalScrollOffset() != 0 && totalItemCount >= lastVisibleItemPosition + 1) {
            if (lastVisibleItemPosition + 1 == totalItemCount) {
                onFinishedScroll()
            } else {
                onScrolling()
            }
        } else onScrolling()
    }

    fun onScrolling() {}

    fun onFinishedScroll() {}

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(view: View?, page: Int, totalItemsCount: Int)
}