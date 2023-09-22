package com.arefdev.base.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.extensions.subscribeVia
import com.arefdev.base.utils.isOsAtLeast
import io.reactivex.rxjava3.core.Observable

/**
 * Updated on 29/07/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
abstract class BaseActivity<VDB : ViewDataBinding> : AppCompatActivity() {

    val binding: VDB by lazy { bindingInflater(layoutInflater).also { it.lifecycleOwner = instance } }
    protected abstract val bindingInflater: (LayoutInflater) -> VDB

    private val mOnTouchOutsideViewListeners = HashMap<Int, OnTouchOutsideViewListenerImpl>()

    open fun fullscreen(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
    }

    val instance: AppCompatActivity
        get() = this

    fun addOnTouchOutsideViewListener(listener: OnTouchOutsideViewListenerImpl) {
        addOnTouchOutsideViewListener(this, listener)
    }

    fun addOnTouchOutsideViewListener(view: View, listener: OnTouchOutsideViewListener) {
        addOnTouchOutsideViewListener(object : OnTouchOutsideViewListenerImpl(view) {
            override fun onTouchOutside(event: MotionEvent) {
                listener.onTouchOutside(event)
            }
        })
    }

    fun addOnTouchOutsideViewListener(lifecycleOwner: LifecycleOwner?, listener: OnTouchOutsideViewListenerImpl) {
        mOnTouchOutsideViewListeners[listener.hashCode()] = listener
        lifecycleOwner?.lifecycle?.addObserver(LifecycleEventObserver { _: LifecycleOwner?, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_PAUSE)
                mOnTouchOutsideViewListeners.remove(listener.hashCode())
            else if (event == Lifecycle.Event.ON_RESUME)
                mOnTouchOutsideViewListeners[listener.hashCode()] = listener
        })
    }

    fun addOnTouchOutsideViewListener(lifecycleOwner: LifecycleOwner?, view: View, listener: OnTouchOutsideViewListener) {
        addOnTouchOutsideViewListener(lifecycleOwner, object : OnTouchOutsideViewListenerImpl(view) {
            override fun onTouchOutside(event: MotionEvent) {
                listener.onTouchOutside(event)
            }
        })
    }

    fun removeOnTouchOutsideViewListener(listener: OnTouchOutsideViewListener) {
        mOnTouchOutsideViewListeners.remove(listener.hashCode())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (fullscreen()) {
            if (isOsAtLeast(SDK_CODES.R)) {
                try {
                    WindowCompat.setDecorFitsSystemWindows(window, true)
                    window?.insetsController?.let {
                        it.hide(WindowInsets.Type.statusBars())
                        it.hide(WindowInsets.Type.navigationBars())
                        it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // Notify touch outside listener if user tapped outside a given view
            Observable.fromIterable(mOnTouchOutsideViewListeners.values).map { listener ->
                if (listener.view.visibility == View.VISIBLE) {
                    val viewRect = Rect()
                    listener.view.getGlobalVisibleRect(viewRect)
                    if (!viewRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        listener.onTouchOutside(ev)
                    }
                }
                listener
            }.subscribeVia()
        }
        return super.dispatchTouchEvent(ev)
    }

    abstract class OnTouchOutsideViewListenerImpl(val view: View) : OnTouchOutsideViewListener {
        abstract override fun onTouchOutside(event: MotionEvent)
    }

    interface OnTouchOutsideViewListener {
        fun onTouchOutside(event: MotionEvent)
    }
}