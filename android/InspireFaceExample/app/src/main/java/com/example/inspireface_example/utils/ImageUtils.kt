package com.example.inspireface_example.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


fun checkBitmapValid(bitmap: Bitmap?): Boolean {
    if (bitmap != null && !bitmap.isRecycled && bitmap.width >0) {
        return true
    }
    return false
}


fun saveBitmap(bitmap: Bitmap, path: String?): Boolean {
    try {
        val file = File(path)
        val parent = file.parentFile
        if (!parent.exists()) {
            parent.mkdirs()
        }
        val fos = FileOutputStream(file)
        val b = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return b
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}

fun releaseBitmap(bitmap: Bitmap?) {
    bitmap?.takeIf { !it.isRecycled }?.recycle()
}








