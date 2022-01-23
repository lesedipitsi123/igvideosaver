package com.devbytes.app.igvideosaver.utils

import android.util.Log
import androidx.annotation.WorkerThread
import com.devbytes.app.igvideosaver.data.entites.InstagramMedia
import com.devbytes.app.igvideosaver.data.entites.enums.MediaType

import org.jsoup.Jsoup
import java.util.*

private const val TAG = "WorkerUtils"

@WorkerThread
fun convertToEntity(resourceUri: String?): InstagramMedia {

    val doc = Jsoup.connect(resourceUri).get()

    val script = doc.tagName("script").textNodes()

    Log.d(TAG, "getVideoEntity: $script")
    return InstagramMedia(
        caption = doc.select("meta[property=og:title]").attr("content"),
        thumbnail = doc.select("meta[property=og:image]").attr("content"),
        url = doc.select("meta[property=og:video]").attr("content"),
        type = convertToMediaType(doc.select("meta[property=og:type]").attr("content")),
        extension = doc.select("meta[property=og:video:type]").attr("content"),
        timestamp = Calendar.getInstance().time
    )
}

fun convertToMediaType(type: String) : MediaType{
    return when(type) {
        "video" -> MediaType.Video
        "image" -> MediaType.Image
        "reel" -> MediaType.Reel
        "story" -> MediaType.Story
        else -> MediaType.Null
    }
}