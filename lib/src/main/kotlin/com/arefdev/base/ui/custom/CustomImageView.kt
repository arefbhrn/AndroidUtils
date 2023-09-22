package com.arefdev.base.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.arefdev.base.R
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer

/**
 * Updated on 07/03/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class CustomImageView : AppCompatImageView {

    private var placeholder: Drawable? = null
    private var isOpenable = false
    private var src: Any? = null
    private var onClickListener: OnClickListener? = null

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView)

        setPlaceholder(typedArray.getDrawable(R.styleable.CustomImageView_placeholder))

        setOpenable(typedArray.getBoolean(R.styleable.CustomImageView_openable, false))

        if (typedArray.hasValue(R.styleable.CustomImageView_url)) {
            src = typedArray.getString(R.styleable.CustomImageView_url)
            showSrc()
        }

        typedArray.recycle()
    }

    override fun setImageResource(resId: Int) {
        src = resId
        super.setImageResource(resId)
    }

    override fun setImageBitmap(bm: Bitmap) {
        src = bm
        super.setImageBitmap(bm)
    }

    override fun setImageURI(uri: Uri?) {
        showSrc()
    }

    fun setPlaceholder(drawable: Drawable?) {
        placeholder = drawable
    }

    fun setOpenable(openable: Boolean) {
        isOpenable = openable
        setOnClickListener()
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
        setOnClickListener()
    }

    private fun setOnClickListener() {
        super.setOnClickListener(null)

        if (isOpenable || onClickListener != null) super.setOnClickListener {
            if (isOpenable)
                open()

            onClickListener?.onClick(this)

        } else
            isClickable = false
    }

    fun setUrl(url: String?) {
        src = when {
            url == null -> null
            !url.startsWith("http") -> url
            else -> Uri.parse(url)
        }
        showSrc()
    }

    private fun showSrc() {
        Glide.with(context)
            .load(src)
            .placeholder(placeholder)
            .fallback(placeholder)
            .into(this)
    }

    private fun open() {
        if (src == null) return
        StfalconImageViewer.Builder(
            context, arrayOf(src!!)
        ) { imageView: ImageView?, image: Any? ->
            Glide.with(context)
                .load(image)
                .into(imageView!!)
        }
            .withBackgroundColorResource(R.color.dark_transparency)
//            .withOverlayView(view)
//            .withImageMarginPixels(margin)
//            .withContainerPaddingPixels(GraphicUtils.convertDpToPx(16))
//            .withContainerPaddingPixels(paddingStart, paddingTop, paddingEnd, paddingBottom)
            .withHiddenStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .withTransitionFrom(this)
            .show()
    }
}