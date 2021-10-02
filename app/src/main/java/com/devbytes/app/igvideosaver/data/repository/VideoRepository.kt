package com.devbytes.app.igvideosaver.data.repository

import androidx.annotation.WorkerThread
import com.devbytes.app.igvideosaver.data.entites.VideoEntity
import com.devbytes.app.igvideosaver.data.room.dao.VideoDao

class VideoRepository(private val videosDao: VideoDao) {

    fun getVideos() = videosDao.get()

    @WorkerThread
    suspend fun insert(entity: VideoEntity) {
        videosDao.insert(entity)
    }
}