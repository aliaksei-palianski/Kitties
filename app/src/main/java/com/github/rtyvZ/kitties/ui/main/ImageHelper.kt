package com.github.rtyvZ.kitties.ui.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.widget.AppCompatImageView
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

object ImageHelper {
    const val DATE_FORMAT = "yyyyMMdd_HHmmss"
    private lateinit var currentPhotoPath: String

    fun createImageFile(context: Context): File {
        val timeStamp: String = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val storageDir: File = context.filesDir
        return File.createTempFile(
            "JPEG_${timeStamp}",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun setPick(view: AppCompatImageView) {
        val targetHeight = view.height
        val targetWidth = view.width

        val bitmapOptions = BitmapFactory.Options().apply {
            //inJustDecodeBounds = true

            val photoWidth = outWidth
            val photoHeight = outHeight

            val scaleFactor: Int =
                max(1, min(photoWidth / targetWidth, photoHeight / targetHeight))

            //  inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        BitmapFactory.decodeFile(currentPhotoPath, bitmapOptions)?.also {
            it.let {
                view.setImageBitmap(rotateBitmap(it))
            }
        }
    }

    private fun rotateBitmap(bitmap: Bitmap): Bitmap? {
        val exifInterface = ExifInterface(currentPhotoPath)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        var rotatedBitmap: Bitmap? = null

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotatedBitmap = rotateImage(bitmap, 90)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotatedBitmap = rotateImage(bitmap, 180)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotatedBitmap = rotateImage(bitmap, 270)
            }
        }
        return rotatedBitmap
    }
}