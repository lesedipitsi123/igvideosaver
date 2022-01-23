package com.devbytes.app.igvideosaver.data.entites

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devbytes.app.igvideosaver.data.entites.enums.MediaType
import kotlinx.parcelize.Parcelize
import java.security.Timestamp
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Entity(tableName = "ig_media_table")
@Parcelize
data class InstagramMedia @Inject constructor(
    val caption: String,
    val thumbnail: String,
    val url: String,
    val type: MediaType,
    val extension: String,
    val timestamp: Date
): Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun getFileExtension() = extension.split('/')[1]
}