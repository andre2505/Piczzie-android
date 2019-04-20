package com.ziggy.kdo.utils

import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/** A basic Camera preview class */
class CameraPreview : CameraListener() {
    override fun onPictureTaken(result: PictureResult) {
        val data = result.data
        val fos = FileOutputStream(getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE))
        fos.write(data)
        fos.close()
    }

    private fun getOutputMediaFile(type: Int): File? {

        val mediaStorageDir = File(Environment.getExternalStorageDirectory(), "prezzie")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile: File

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
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
}