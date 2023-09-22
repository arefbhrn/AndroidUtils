package com.arefdev.base.utils

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import com.arefdev.base.enums.SDK_CODES
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Updated on 21/09/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object FileManager {

    fun exists(path: String): Boolean {
        return File(path).exists()
    }

    fun hideFolderFromGallery(path: String): Boolean {
        val dir = File(path)
        return dir.exists() && makeFile("$path.nomedia")
    }

    fun makeDirectory(path: String): Boolean {
        val dir = File(path)
        return !dir.exists() && dir.mkdir()
    }

    fun makeDirectories(path: String): Boolean {
        val dir = File(path)
        return !dir.exists() && dir.mkdirs()
    }

    fun makeFile(path: String): Boolean {
        val file = File(path)
        if (!file.exists()) {
            try {
                return file.createNewFile()
            } catch (ignored: IOException) {
            }
        }
        return false
    }

    fun copy(src: String, des: String): Boolean {
        return try {
            val `in` = File(src)
            val out = File(des)
            makeFile(src)
            val stream = DataInputStream(FileInputStream(`in`))
            val buffer = ByteArray(`in`.length().toInt())
            stream.readFully(buffer)
            stream.close()
            val fos = DataOutputStream(FileOutputStream(out))
            fos.write(buffer)
            fos.flush()
            fos.close()
            true
        } catch (e: FileNotFoundException) {
            false // swallow a 404
        } catch (e: IOException) {
            false // swallow a 404
        }
    }

    fun copy(src: File, des: File?): Boolean {
        return try {
            val stream = DataInputStream(FileInputStream(src))
            val buffer = ByteArray(src.length().toInt())
            stream.readFully(buffer)
            stream.close()
            val fos = DataOutputStream(FileOutputStream(des))
            fos.write(buffer)
            fos.flush()
            fos.close()
            true
        } catch (e: FileNotFoundException) {
            false // swallow a 404
        } catch (e: IOException) {
            false // swallow a 404
        }
    }

    @Throws(IOException::class)
    fun copyFile(src: String, des: String) {
        val inputFile = File(src)
        val outputFile = File(des)
        val inStream = FileInputStream(inputFile)
        val outStream = FileOutputStream(outputFile)
        val inChannel = inStream.channel
        val outChannel = outStream.channel
        inChannel.transferTo(0, inChannel.size(), outChannel)
        inStream.close()
        outStream.close()
    }

    @Throws(IOException::class)
    fun copyFile(src: File?, des: File?) {
        val inStream = FileInputStream(src)
        val outStream = FileOutputStream(des)
        val inChannel = inStream.channel
        val outChannel = outStream.channel
        inChannel.transferTo(0, inChannel.size(), outChannel)
        inStream.close()
        outStream.close()
    }

    fun copyStream(inputStream: InputStream, os: OutputStream) {
        val bufferSize = 1024
        try {
            val bytes = ByteArray(bufferSize)
            while (true) {
                val count = inputStream.read(bytes, 0, bufferSize)
                if (count == -1) break
                os.write(bytes, 0, count)
            }
        } catch (ignored: Exception) {
        }
    }

    fun deleteFile(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.delete()
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            children?.also {
                for (i in it.indices) {
                    val success = deleteDir(File(dir, it[i]))
                    if (!success) {
                        return false
                    }
                }
            }
            dir.delete()

        } else if (dir != null && dir.isFile) {
            dir.delete()

        } else {
            false
        }
    }

    fun move(src: String, des: String): Boolean {
        return copy(src, des) && deleteFile(src)
    }

    fun move(src: File, des: File): Boolean {
        return copy(src.path, des.path) && deleteFile(src.path)
    }

    fun getFiles(directory: String): List<String> {
        val lstFiles = mutableListOf<String>()
        val dir = File(directory)
        if (!dir.isDirectory) return lstFiles
        val files = dir.listFiles()
        files?.also {
            for (file in files)
                if (!file.isDirectory)
                    lstFiles.add(file.name)
        }
        return lstFiles
    }

    // searches subfolders too
    fun getListFiles(parentDir: File): List<File> {
        val inFiles = mutableListOf<File>()
        val files = parentDir.listFiles()
        files?.also {
            for (file in files) {
                if (file.isDirectory) {
                    inFiles.addAll(getListFiles(file))
                } else {
                    inFiles.add(file)
                }
            }
        }
        return inFiles
    }

    fun getPathSize(f: File): Long {
        var size: Long = 0
        if (f.isDirectory) {
            f.listFiles()?.also {
                for (file in it) {
                    size += getPathSize(file)
                }
            }
        } else {
            size = f.length()
        }
        return size
    }

    fun clearCache(context: Context): Boolean {
        try {
            val dir = context.cacheDir
            return deleteDir(dir)
        } catch (ignored: Exception) {
        }
        return false
    }

    @Throws(IOException::class, ActivityNotFoundException::class)
    fun openFile(context: Context, file: File) {
        if (!file.exists()) throw IOException()

        // Create URI
        val uri = Uri.fromFile(file)
        val extention = file.toString().substring(file.toString().lastIndexOf("."))
        val intent = Intent(Intent.ACTION_VIEW)
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (extention == ".doc" || extention == ".docx") {
            // Word document
            intent.setDataAndType(uri, "application/msword")
        } else if (extention == ".pdf") {
            // PDF file
            intent.setDataAndType(uri, "application/pdf")
        } else if (extention == ".ppt" || extention == ".pptx") {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        } else if (extention == ".xls" || extention == ".xlsx") {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel")
        } else if (extention == ".zip" || extention == ".rar") {
            // ZIP file
            intent.setDataAndType(uri, "application/zip")
        } else if (extention == ".rtf") {
            // RTF file
            intent.setDataAndType(uri, "application/rtf")
        } else if (extention == ".wav" || extention == ".mp3") {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav")
        } else if (extention == ".gif") {
            // GIF file
            intent.setDataAndType(uri, "image/gif")
        } else if (extention == ".jpg" || extention == ".jpeg" || extention == ".png") {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg")
        } else if (extention == ".txt") {
            // Text file
            intent.setDataAndType(uri, "text/plain")
        } else if (extention == ".3gp" || extention == ".mpg" || extention == ".mpeg" || extention == ".mpe" ||
            file.toString().contains(".mp4") || file.toString().contains(".avi")
        ) {
            // Video files
            intent.setDataAndType(uri, "video/*")
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun getUriFromFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    }

    fun getMimeType(context: Context, file: File): String? {
        return context.contentResolver.getType(getUriFromFile(context, file))
    }

    @RequiresApi(api = SDK_CODES.Q)
    @Throws(IOException::class)
    fun saveInDownloads(context: Context, file: File, childDir: String? = null) {
        val collection =
            if (isOsAtLeast(SDK_CODES.Q))
                MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
        saveFile(context, collection, file, "Download", childDir)
    }

    @RequiresApi(api = SDK_CODES.Q)
    @Throws(IOException::class)
    fun saveInPictures(context: Context, file: File, childDir: String? = null) {
        val collection =
            if (isOsAtLeast(SDK_CODES.Q))
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        saveFile(context, collection, file, Environment.DIRECTORY_PICTURES, childDir)
    }

    @RequiresApi(api = SDK_CODES.Q)
    @Throws(IOException::class)
    fun saveInMusics(context: Context, file: File, childDir: String? = null) {
        val collection =
            if (isOsAtLeast(SDK_CODES.Q))
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        saveFile(context, collection, file, Environment.DIRECTORY_MUSIC, childDir)
    }

    @RequiresApi(api = SDK_CODES.Q)
    @Throws(IOException::class)
    fun saveInVideos(context: Context, file: File, childDir: String? = null) {
        val collection =
            if (isOsAtLeast(SDK_CODES.Q))
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            else
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        saveFile(context, collection, file, Environment.DIRECTORY_MOVIES, childDir)
    }

    @RequiresApi(api = SDK_CODES.Q)
    @Throws(IOException::class)
    fun saveFile(context: Context, collection: Uri, file: File, mainDir: String, childDir: String? = null) {
        val dir = mainDir + if (childDir != null) File.separator + childDir else ""
        val mimeType = getMimeType(context, file)
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, dir)
        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        values.put(MediaStore.MediaColumns.IS_PENDING, 1)
        val resolver = context.contentResolver
        val uri = resolver.insert(collection, values)
        val os = resolver.openOutputStream(uri!!)
        Files.copy(Paths.get(file.toURI()), os)
        values.clear()
        values.put(MediaStore.MediaColumns.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }

    @WorkerThread
    fun download(url: String, dst: File) {
        var count: Int
        try {
            val mUrl = URL(url)
            val conection = mUrl.openConnection()
            conection.connect()
            val input = conection.getInputStream()
            if (dst.parentFile!!.exists().not())
                dst.parentFile?.mkdir()
            val output: OutputStream = FileOutputStream(dst)
            val data = ByteArray(1024)
            while (input.read(data).also { count = it } != -1) {
                output.write(data, 0, count)
            }
            output.flush()
            output.close()
            input.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
