package com.ziggy.kdo.utils

import android.content.Context
import android.net.Uri
import android.os.RecoverySystem
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import android.webkit.MimeTypeMap





class ProgressRequestBody(var context: Context, var file: File, progressListener: ProgressListener) : RequestBody() {

    private val DEFAULT_BUFFER_SIZE = 4096
    private val UPDATE_PERCENT_THRESHOLD = 1

    private var mediaType: MediaType? = null

    var listener: ProgressListener? = progressListener

    init {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        mediaType = MediaType.parse(type!!)
    }

    interface ProgressListener {
        fun onUploadProgress(progressInPercent: Int, totalBytes: Long)
    }

    override fun contentType(): MediaType? {
        return mediaType
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val totalBytes = file.length()
        val input = FileInputStream(file)

        try {
            // init variables
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var uploadedBytes: Long = 0
            var readBytes = 0
            var fileUploadedInPercent = 0

            // go through the file and notify the UI
            while ({readBytes = input.read(buffer);readBytes}() != -1) {
                // notify UI at max after every 1%
                val newfileUploadedInPercent = (uploadedBytes * 100 / totalBytes).toInt()
                if (fileUploadedInPercent + UPDATE_PERCENT_THRESHOLD <= newfileUploadedInPercent) {
                    fileUploadedInPercent = newfileUploadedInPercent
                    listener?.onUploadProgress(newfileUploadedInPercent, totalBytes)
                }

                uploadedBytes += readBytes.toLong()
                sink.write(buffer, 0, readBytes)
            }
        } finally {
            input.close()
        }
        listener?.onUploadProgress(100, totalBytes);
    }

    companion object {

        var mProgressListener: ProgressListener? = null

        fun callbackUpload(progressListener: ProgressListener){
            mProgressListener = progressListener
        }
    }

}