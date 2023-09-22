package com.arefdev.base.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import eightbitlab.com.blurview.BlurAlgorithm
import eightbitlab.com.blurview.RenderEffectBlur
import eightbitlab.com.blurview.RenderScriptBlur

/**
 * Updated on 07/10/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
class BlurRenderer(context: Context) : BlurAlgorithm {

    private val renderer: BlurAlgorithm

    init {
        this.renderer =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                RenderEffectBlur()
            else
                RenderScriptBlur(context)
    }

    override fun blur(bitmap: Bitmap?, blurRadius: Float): Bitmap {
        return renderer.blur(bitmap, blurRadius)
    }

    override fun destroy() {
        renderer.destroy()
    }

    override fun canModifyBitmap(): Boolean {
        return renderer.canModifyBitmap()
    }

    override fun getSupportedBitmapConfig(): Bitmap.Config {
        return renderer.supportedBitmapConfig
    }

    override fun scaleFactor(): Float {
        return renderer.scaleFactor()
    }

    override fun render(canvas: Canvas, bitmap: Bitmap) {
        renderer.render(canvas, bitmap)
    }
}