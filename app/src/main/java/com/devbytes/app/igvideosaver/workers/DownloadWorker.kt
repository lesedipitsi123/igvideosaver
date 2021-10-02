package com.devbytes.app.igvideosaver.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread

import com.devbytes.app.igvideosaver.utils.*
import com.devbytes.app.igvideosaver.R
import com.devbytes.app.igvideosaver.SocialGrabApplication
import com.devbytes.app.igvideosaver.utils.ConstantsUtils.KEY_VIDEO_LINK
import com.devbytes.app.igvideosaver.utils.ConstantsUtils.VIDEO_TYPE
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DownloadWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    companion object {
        private const val TAG = "DownloadWorker"
    }

    @WorkerThread
    override fun doWork(): Result {
        return runBlocking {
            val appContext = applicationContext

            val resourceUri = inputData.getString(KEY_VIDEO_LINK)

            try {
                if (TextUtils.isEmpty(resourceUri)) {
                    Log.e(TAG, "Invalid input uri")
                    throw IllegalArgumentException("Invalid input uri")
                }

                val timestamp = with(Calendar.getInstance()) {
                    SimpleDateFormat("yyyyddMMHHmmss", Locale.getDefault()).format(time)
                }

                val entity = getVideoEntity(resourceUri)

                if (entity.type != VIDEO_TYPE)
                    return@runBlocking Result.failure()

                (applicationContext as SocialGrabApplication).repository.insert(entity)

                val fileName =
                    String.format(
                        Locale.getDefault(),
                        "ig_video_%s.%s",
                        timestamp,
                        entity.getFileExtension()
                    )
                val uri: Uri = Uri.parse(entity.link)

                val request = DownloadManager.Request(uri).apply {
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

                Result.success()
            } catch (throwable: Throwable) {
                Log.e(TAG, "Error downloading file")
                throwable.printStackTrace()
                Result.failure()
            }
        }
    }
}