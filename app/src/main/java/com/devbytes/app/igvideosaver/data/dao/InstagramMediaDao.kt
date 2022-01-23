package com.devbytes.app.igvideosaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.devbytes.app.igvideosaver.data.entites.InstagramMedia
import com.devbytes.app.igvideosaver.data.entites.enums.MediaType

@Dao
interface InstagramMediaDao {
    @Query("SELECT * FROM ig_media_table ORDER BY timestamp DESC")
    fun get() : LiveData<List<InstagramMedia>>

    @Query("SELECT * FROM ig_media_table WHERE type = :mediaType ORDER BY timestamp DESC")
    fun getByMediaType(mediaType: MediaType) : LiveData<List<InstagramMedia>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg entity: InstagramMedia)

    @Update
    suspend fun update(entity: InstagramMedia)

    @Delete
    suspend fun delete(vararg entity: InstagramMedia)
}