package com.arefdev.base.utils

import android.content.Context
import android.graphics.Bitmap.CompressFormat
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.utils.advancedluban.Luban
import java.io.File

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object ImageUtils {

    fun compress(context: Context, image: File) {
        val format = when (image.name.substring(image.name.lastIndexOf(".") + 1).lowercase()) {
            "jpg", "jpeg" -> CompressFormat.JPEG
            "png", "webp" ->
                if (isOsAtLeast(SDK_CODES.R))
                    CompressFormat.WEBP_LOSSLESS
                else
                    CompressFormat.WEBP

            else -> return
        }
        compress(context, image, format)
    }

    fun compress(context: Context, image: File, format: CompressFormat? = null) {
        compress(context, image, image, format)
    }

    fun compress(context: Context, src: File, dst: File = src, format: CompressFormat? = CompressFormat.JPEG) {
        val file: File = Luban.compress(context, src)
            .putGear(Luban.THIRD_GEAR)
            .setCompressFormat(format)
            .asObservable()
            .blockingFirst()
        if (file.path != dst.path)
            FileManager.move(file, dst)
    }
}
