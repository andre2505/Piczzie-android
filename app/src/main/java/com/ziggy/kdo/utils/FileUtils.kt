package com.ziggy.kdo.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.ziggy.kdo.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    fun getOutputMediaFile(context: Context, type: Int): File? {

        val mediaStorageDir = File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name))
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile: File

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(context.getString(R.string.app_name), "failed to create directory")
                return null
            }
        }

        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        } else {
            return null
        }

        return mediaFile
    }


    fun createMultiPartBodyImage(file: File?): MultipartBody.Part {

        val uri = Uri.fromFile(file)

        val requestFile = RequestBody.create(MediaType.parse(uri.toString()), file!!)

        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
    /*fun getFile(context: Context, file: File): File {

    }*/
}