package com.devbytes.app.igvideosaver.tasks

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log

import com.devbytes.app.igvideosaver.utils.*
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.utils.Constants.KEY_VIDEO_LINK


class DownloadWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    companion object {
        private const val TAG = "DownloadWorker"
    }

    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_VIDEO_LINK)

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val downloadLink = getInstagramVideoLink(resourceUri)

            val uri: Uri = Uri.parse(downloadLink)

            val request = DownloadManager.Request(uri).apply {
                setTitle(appContext.getString(R.string.app_name))
                setDescription(appContext.getString(R.string.download_in_progress))
                setDestinationInExternalFilesDir(appContext, Environment.DIRECTORY_DOWNLOADS, "test.mp4")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            }

            val downloadManager = appContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            downloadManager.enqueue(request)

            Result.success()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error downloading file")
            throwable.printStackTrace()
            Result.failure()
        }
    }

    override fun onStopped() {
        super.onStopped()
    }
}