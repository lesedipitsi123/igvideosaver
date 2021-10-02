package com.devbytes.app.igvideosaver.viewmodels

import androidx.lifecycle.*
import com.devbytes.app.igvideosaver.data.entites.VideoEntity
import com.devbytes.app.igvideosaver.data.repository.VideoRepository
import kotlinx.coroutines.launch

class VideoViewModel(private val repository: VideoRepository) : ViewModel() {

    private var _videosLiveData : LiveData<List<VideoEntity>> = MutableLiveData()
    val videosLiveData get() = _videosLiveData

    fun get() {
        _videosLiveData = repository.getVideos().asLiveData()
    }

    fun insert(entity: VideoEntity) = viewModelScope.launch {
        repository.insert(entity)
    }
}

class VideoViewModelFactory(private val repository: VideoRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            return VideoViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}