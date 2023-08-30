package com.compfest.aiapplication

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.compfest.aiapplication.data.Prediction
import com.compfest.aiapplication.data.PredictionRepository
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun uriToBitmap(uri: Uri, context: Context): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
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

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun saveImage(bitmap: Bitmap?, context: Context) {
    bitmap?.let {
        val externalDir = context.getExternalFilesDir(null)
        if (externalDir != null) {
            val imageFile = File(externalDir, "captured_image.jpg")
            try {
                val outputStream = FileOutputStream(imageFile)
                it.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
                Log.d("Saving", "Success")
                Toast.makeText(context, "ImageSaved", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("Saving", "Fail")
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.d("Saving", "Unavailable")
            Toast.makeText(context, "External storage not available", Toast.LENGTH_SHORT).show()
        }
    } ?: run {
        Log.d("Saving", "No image")
        Toast.makeText(context, "No image to save", Toast.LENGTH_SHORT).show()
    }
}
