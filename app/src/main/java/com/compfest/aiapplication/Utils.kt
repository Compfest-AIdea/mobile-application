package com.compfest.aiapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun getImageFromExternalStorage(imagePath: String): Bitmap? {
    try {
        val imageFile = File(imagePath)
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.absolutePath)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun saveImageToExternalStorage(context: Context, capturedImage: Bitmap?): String? {
    capturedImage?.let { bitmap ->
        val externalDir = context.getExternalFilesDir(null)

        if (externalDir != null) {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "captured_image_$timestamp.jpg"
            val imageFile = File(externalDir, imageFileName)
            try {
                val fos = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()

                val imagePath = imageFile.absolutePath

                Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()
                return imagePath
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                context,
                "External storage not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    } ?: run {
        Toast.makeText(context, "No image to save", Toast.LENGTH_SHORT).show()
    }

    return null
}
