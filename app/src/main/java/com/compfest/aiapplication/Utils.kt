package com.compfest.aiapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.Toast
import com.compfest.aiapplication.data.FaQ
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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

fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val scaleWidth = maxWidth.toFloat() / width
    val scaleHeight = maxHeight.toFloat() / height

    val scaleFactor = minOf(scaleWidth, scaleHeight)

    val matrix = Matrix()
    matrix.postScale(scaleFactor, scaleFactor)

    return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
}

fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}

fun convertLongToDateString(timeInMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
    dateFormat.timeZone = TimeZone.getDefault() // Set zona waktu lokal
    val date = Date(timeInMillis)
    return dateFormat.format(date)
}

fun loadJsonFromAssets(context: Context, fileName: String): String {
    return try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun loadFaqDataFromJson(context: Context): List<FaQ> {
    val jsonFile = loadJsonFromAssets(context, "faq_list.json")
    return Json.decodeFromString(jsonFile)
}
