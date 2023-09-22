package com.arefdev.base.utils

import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import com.arefdev.base.BaseApp.Companion.context
import com.arefdev.base.utils.FileManager.getMimeType
import java.io.File

class SingleMediaScanner private constructor(private val mFile: File) : MediaScannerConnectionClient {

    companion object {
        fun scan(file: File) {
            SingleMediaScanner(file).scan()
        }
    }

    private val mMs: MediaScannerConnection = MediaScannerConnection(context, this)

    private fun scan() {
        mMs.connect()
    }

    override fun onMediaScannerConnected() {
        val mimeType = getMimeType(context, mFile)
        mMs.scanFile(mFile.absolutePath, mimeType)
    }

    override fun onScanCompleted(paramString: String, paramUri: Uri) {
        mMs.disconnect()
    }
}