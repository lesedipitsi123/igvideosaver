package com.devbytes.app.igvideosaver.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.devbytes.app.igvideosaver.tasks.DownloadWorker
import com.devbytes.app.igvideosaver.utils.Constants.KEY_VIDEO_LINK
import com.devbytes.app.igvideosaver.utils.Constants.TAG_OUTPUT

class DownloadViewModel(application: Application) : AndroidViewModel(application) {
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfo: LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)

    internal fun downloadVideo(link : String = "https://www.instagram.com/p/CTt8mj5I10r/?utm_source=ig_web_copy_link") {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workerRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(constraints)
            .setInputData(Data.Builder().putString(KEY_VIDEO_LINK, link).build())
            .build()

        workManager.enqueue(workerRequest)
    }
}