package com.arefdev.base.ui.base

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator

///**
// * Updated on 05/03/2022
// *
// * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
// */

@BindingAdapter("app:srcCompat")
fun bindImage(imageView: ImageView, drawable: Drawable?) {
    imageView.setImageDrawable(drawable)
}

@BindingAdapter("app:srcCompat")
fun bindImage(imageView: ImageView?, url: String?) {
    Glide.with(imageView!!).load(url).into(imageView)
}

@BindingAdapter("app:tint")
fun bindImageTint(imageView: ImageView, color: Int) {
    imageView.setColorFilter(color)
}

@BindingAdapter("app:tint")
fun bindTint(imageButton: ImageButton, @ColorInt color: Int) {
    imageButton.setColorFilter(color)
}

@BindingAdapter("android:layout_weight")
fun bindWeight(view: View, weight: Float) {
    try {
        val lp = view.layoutParams as LinearLayout.LayoutParams
        lp.weight = weight
        view.layoutParams = lp
    } catch (ignored: Exception) {
    }
}

@BindingAdapter("android:visibility")
fun bindVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("android:layout_margin")
fun bindMargin(view: View, margin: Int) {
    val params = view.layoutParams
    if (params is MarginLayoutParams) {
        val margins = params
        margins.topMargin = margin
        margins.rightMargin = margin
        margins.bottomMargin = margin
        margins.leftMargin = margin
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginTop")
fun bindMarginTop(view: View, margin: Int) {
    val params = view.layoutParams
    if (params is MarginLayoutParams) {
        params.topMargin = margin
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginRight")
fun bindMarginRight(view: View, margin: Int) {
    val params = view.layoutParams
    if (params is MarginLayoutParams) {
        params.rightMargin = margin
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginBottom")
fun bindMarginBottom(view: View, margin: Int) {
    val params = view.layoutParams
    if (params is MarginLayoutParams) {
        params.bottomMargin = margin
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginLeft")
fun bindMarginLeft(view: View, margin: Int) {
    val params = view.layoutParams
    if (params is MarginLayoutParams) {
        params.leftMargin = margin
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginStat")
fun bindMarginStart(view: View, margin: Int) {
    val params = view.layoutParams
    if (params is MarginLayoutParams) {
        params.marginStart = margin
        view.requestLayout()
    }
}

@BindingAdapter("android:layout_marginEnd")
fun bindMarginEnd(view: View, margin: Int) {
    val params = view.layoutParams
    if (params is MarginLayoutParams) {
        params.marginEnd = margin
        view.requestLayout()
    }
}

@BindingAdapter("app:indicatorColor")
fun bindColor(view: CircularProgressIndicator, color: String) {
    view.setIndicatorColor(Color.parseColor(color))
}

@BindingAdapter("app:refreshing")
fun bindRefreshing(view: SwipeRefreshLayout, refreshing: Boolean) {
    view.isRefreshing = refreshing
}

@BindingAdapter("app:layout_constraintHorizontal_weight")
fun bindHorizontalWeight(view: FrameLayout, value: Float) {
    val lp = view.layoutParams as ConstraintLayout.LayoutParams
    lp.horizontalWeight = value
    view.layoutParams = lp
}
