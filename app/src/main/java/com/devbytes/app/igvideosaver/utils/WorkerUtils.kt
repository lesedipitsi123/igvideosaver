package com.devbytes.app.igvideosaver.utils

import androidx.annotation.WorkerThread
import com.devbytes.app.igvideosaver.data.entites.VideoMeta

import org.jsoup.Jsoup

private const val TAG = "WorkerUtils"

@WorkerThread
fun getVideoMetaData(resourceUri: String?): VideoMeta {

    val doc = Jsoup.connect(resourceUri).get()

    return VideoMeta(
        caption = doc.select("meta[property=og:title]").attr("content"),
        thumbnail = doc.select("meta[property=og:image]").attr("content"),
        link = doc.select("meta[property=og:video]").attr("content"),
        type = doc.select("meta[property=og:type]").attr("content"),
        extension = doc.select("meta[property=og:video:type]").attr("content"))
}