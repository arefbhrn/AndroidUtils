package com.arefdev.base.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class NonSwipableViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    var isSwipeEnabled = true

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isSwipeEnabled && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return isSwipeEnabled && super.onInterceptTouchEvent(event)
    }
}
