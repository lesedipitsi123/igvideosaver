package com.devbytes.app.igvideosaver.workers

import android.content.Context
import androidx.work.WorkerParameters
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker

import com.devbytes.app.igvideosaver.utils.*
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.data.entites.enums.MediaType
import com.devbytes.app.igvideosaver.data.repository.InstagramMediaRepository
import com.devbytes.app.igvideosaver.utils.ConstantsUtils.KEY_URL
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: InstagramMediaRepository
) : CoroutineWorker(appContext, params) {
    companion object {
        private const val TAG = "DownloadWorker"
    }

    @WorkerThread
    override suspend fun doWork(): Result {
        val resourceUri = inputData.getString(KEY_URL)

        try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val entity = convertToEntity(resourceUri)

            if (entity.type == MediaType.Null) {
                return Result.failure()
            }

            val timestamp = with(Calendar.getInstance()) {
                SimpleDateFormat("yyyyddMMHHmmss", Locale.getDefault()).format(time)
            }

            val fileName =
                String.format(
                    Locale.getDefault(),
                    "ig_video_%s.%s",
                    timestamp,
                    entity.getFileExtension()
                )

            val request = DownloadManager.Request(Uri.parse(entity.url)).apply {
                setTitle(fileName)
                setDescription(appContext.getString(R.string.app_name))
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    File.separator + "InstagramSaver" + File.separator + fileName
                )
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            }

            val downloadManager =
                appContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            repository.insert(entity)

            return Result.success()

        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Log.e(TAG, "Error downloading file")

            return Result.failure()
        }
    }
}