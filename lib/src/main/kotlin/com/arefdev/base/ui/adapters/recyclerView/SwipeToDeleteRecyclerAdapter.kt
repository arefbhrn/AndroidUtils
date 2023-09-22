package com.arefdev.base.ui.adapters.recyclerView

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.arefdev.base.R
import com.arefdev.base.ui.adapters.recyclerView.base.BaseListAdapter
import com.arefdev.base.utils.SwipeToDeleteListItemTouchHelper
import com.arefdev.base.utils.SwipeToDeleteListItemTouchHelper.ItemSwipedListener
import com.google.android.material.snackbar.Snackbar

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class SwipeToDeleteRecyclerAdapter<T, VDB : ViewDataBinding> protected constructor(
    private val activity: FragmentActivity,
    private val lifecycleOwner: LifecycleOwner,
    list: List<T>,
    private val type: String? = ""
) : BaseListAdapter<T, VDB>(activity, list) {

    private var lifecycleObserver: LifecycleEventObserver? = null
    private var recyclerView: RecyclerView? = null
    private var swipeToDeleteListItemTouchHelper: SwipeToDeleteListItemTouchHelper? = null
    private var mRecentlyDeletedItem: T? = null
    private var mRecentlyDeletedItemPosition = -1
    private var snackbar: Snackbar? = null
    private var snackbarIsShowing = false
    private var swipeToDeleteEnabled = true

    protected constructor(activity: FragmentActivity, list: List<T>) : this(activity, activity, list)

    protected constructor(activity: FragmentActivity, list: List<T>, type: String?) : this(activity, activity, list, type)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView

        swipeToDeleteListItemTouchHelper =
            SwipeToDeleteListItemTouchHelper(recyclerView, object : ItemSwipedListener {
                override fun onSwiped(position: Int) {
                    remove(position)
                }
            })

        setSwipeToDeleteEnabled(swipeToDeleteEnabled)

        if (lifecycleObserver == null) {
            lifecycleObserver = object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        if (snackbarIsShowing) snackbar!!.dismiss()
                        lifecycleOwner.lifecycle.removeObserver(this)
                    }
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver!!)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        lifecycleOwner.lifecycle.removeObserver(lifecycleObserver!!)
    }

    fun setSwipeToDeleteEnabled(enabled: Boolean) {
        swipeToDeleteEnabled = enabled
        swipeToDeleteListItemTouchHelper?.setEnabled(enabled)
    }

    fun isSwipeToDeleteEnabled(): Boolean {
        return swipeToDeleteEnabled
    }

    protected abstract fun onItemRemoved(t: T?, position: Int)
    protected abstract fun onUndoItemRemove(t: T?, position: Int)
    override fun remove(position: Int) {
        if (snackbarIsShowing && mRecentlyDeletedItemPosition >= 0) {
            doRemoveCompletely()
            snackbarIsShowing = false
        }
        mRecentlyDeletedItem = getItem(position)
        mRecentlyDeletedItemPosition = position
        super.remove(position)
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {
        val view = activity.window.decorView.findViewById<View>(android.R.id.content)
        snackbar = Snackbar.make(view, String.format("%s Removed.", type), Snackbar.LENGTH_LONG)
        snackbar!!.setAction(context.getText(R.string.cancel)) { undoDelete() }
        snackbar!!.setTextColor(Color.WHITE)
        snackbar!!.setActionTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryLight))
        snackbar!!.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event != DISMISS_EVENT_ACTION && event != DISMISS_EVENT_CONSECUTIVE)
                    doRemoveCompletely()
                snackbarIsShowing = false
            }
        })
        snackbar!!.show()
        snackbarIsShowing = true
    }

    private fun doRemoveCompletely() {
        try {
            onItemRemoved(mRecentlyDeletedItem, mRecentlyDeletedItemPosition)
        } catch (ignored: Exception) {
        }
    }

    private fun undoDelete() {
        add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem!!)
        onUndoItemRemove(mRecentlyDeletedItem, mRecentlyDeletedItemPosition)
        if (mRecentlyDeletedItemPosition == 0) recyclerView!!.smoothScrollToPosition(mRecentlyDeletedItemPosition)
    }
}