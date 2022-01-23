package com.devbytes.app.igvideosaver.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import com.devbytes.app.igvideosaver.data.entites.InstagramMedia
import com.devbytes.app.igvideosaver.data.entites.enums.MediaType.Video
import com.devbytes.app.igvideosaver.data.repository.InstagramMediaRepository
import com.devbytes.app.igvideosaver.utils.ConstantsUtils.KEY_URL
import com.devbytes.app.igvideosaver.workers.DownloadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramMediaViewModel @Inject constructor(
    application: Application,
    private val repository: InstagramMediaRepository
) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)
    private var _videosLiveData: LiveData<List<InstagramMedia>> = MutableLiveData()

    val videosLiveData
        get() = _videosLiveData

    fun getVideos() = viewModelScope.launch {
        _videosLiveData = repository.getByMediaType(Video)
    }

    fun insert(entity: InstagramMedia) = viewModelScope.launch {
        repository.insert(entity)
    }

    fun fetch(url: String) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workerRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(constraints)
            .setInputData(Data.Builder().putString(KEY_URL, url).build())
            .build()

        workManager.enqueue(workerRequest)
    }
}