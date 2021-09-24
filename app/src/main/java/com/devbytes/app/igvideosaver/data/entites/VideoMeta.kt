package com.devbytes.app.igvideosaver.data.entites

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoMeta(
    val caption: String = "",
    val thumbnail: String = "",
    val link: String = "",
    val type: String = "video",
    val extension: String = "mp4"
) : Parcelable {

    fun getFileExtension() = extension.split('/')[1]
}