package com.devbytes.app.igvideosaver.data.repository

import com.devbytes.app.igvideosaver.data.dao.InstagramMediaDao
import com.devbytes.app.igvideosaver.data.entites.InstagramMedia
import com.devbytes.app.igvideosaver.data.entites.enums.MediaType
import javax.inject.Inject

class InstagramMediaRepository @Inject constructor(private val mediaDao: InstagramMediaDao) {

    fun get() = mediaDao.get()

    fun getByMediaType(mediaType: MediaType) = mediaDao.getByMediaType(mediaType)

    suspend fun insert(entity: InstagramMedia) = mediaDao.insertAll(entity)

    suspend fun insertMany(entity: InstagramMedia) = mediaDao.insertAll(entity)

    suspend fun delete(entity: InstagramMedia) = mediaDao.delete(entity)
}