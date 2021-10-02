package com.devbytes.app.igvideosaver.data.entites

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "video_table")
@Parcelize
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var caption: String = "",
    var thumbnail: String = "",
    var link: String = "",
    var type: String = "video",
    var extension: String = "mp4",
    var date: Date? = null
) : Parcelable {
    fun getFileExtension() = extension.split('/')[1]
}