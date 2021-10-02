package com.devbytes.app.igvideosaver.utils

import android.util.Log
import androidx.annotation.WorkerThread
import com.devbytes.app.igvideosaver.data.entites.VideoEntity

import org.jsoup.Jsoup
import java.util.*

private const val TAG = "WorkerUtils"

@WorkerThread
fun getVideoEntity(resourceUri: String?): VideoEntity {

    val doc = Jsoup.connect(resourceUri).get()

    val script = doc.tagName("script").textNodes()

    Log.d(TAG, "getVideoEntity: $script")
    return VideoEntity(
        caption = doc.select("meta[property=og:title]").attr("content"),
        thumbnail = doc.select("meta[property=og:image]").attr("content"),
        link = doc.select("meta[property=og:video]").attr("content"),
        type = doc.select("meta[property=og:type]").attr("content"),
        extension = doc.select("meta[property=og:video:type]").attr("content"),
        date = Calendar.getInstance().time
    )
}