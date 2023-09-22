package com.arefdev.base.ui.custom

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.arefdev.base.R
import com.arefdev.base.databinding.CustomIconButtonBinding
import com.arefdev.base.utils.pxToDp

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class IconButton : FrameLayout {

    private lateinit var binding: CustomIconButtonBinding

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        binding = CustomIconButtonBinding.inflate(LayoutInflater.from(context), this, true)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconButton)

        if (typedArray.hasValue(R.styleable.IconButton_backgroundColor))
            setBackgroundColor(
                typedArray.getColor(
                    R.styleable.IconButton_backgroundColor,
                    ContextCompat.getColor(context, R.color.colorPrimary)
                )
            )

        if (typedArray.hasValue(R.styleable.IconButton_srcCompat))
            setIcon(typedArray.getResourceId(R.styleable.IconButton_srcCompat, 0))

        if (typedArray.hasValue(R.styleable.IconButton_tint))
            setTint(typedArray.getColor(R.styleable.IconButton_tint, 0))

        if (typedArray.hasValue(R.styleable.IconButton_radius))
            setRadius(typedArray.getDimension(R.styleable.IconButton_radius, pxToDp(8f)))

        if (typedArray.hasValue(R.styleable.IconButton_elevation))
            elevation = typedArray.getDimension(R.styleable.IconButton_elevation, 0f)

        if (typedArray.hasValue(R.styleable.IconButton_iconPadding))
            setIconPadding(typedArray.getDimension(R.styleable.IconButton_iconPadding, 0f).toInt())

        typedArray.recycle()
    }

    override fun setBackgroundColor(color: Int) {
        binding.bg.setCardBackgroundColor(color)
    }

    fun setIcon(resId: Int) {
        setIcon(ResourcesCompat.getDrawable(resources, resId, null))
    }

    fun setIcon(icon: Drawable?) {
        binding.img.setImageDrawable(icon)
    }

    fun setTint(color: Int) {
        ImageViewCompat.setImageTintList(binding.img, ColorStateList.valueOf(color))
    }

    fun setRadius(radius: Float) {
        binding.bg.radius = radius
    }

    override fun setElevation(elevation: Float) {
        binding.bg.cardElevation = elevation
    }

    fun setIconPadding(padding: Int) {
        binding.img.setPadding(padding, padding, padding, padding)
    }

    companion object {
        @BindingAdapter("app:srcCompat")
        fun bindImage(view: IconButton, drawable: Drawable?) {
            view.setIcon(drawable)
        }
    }
}