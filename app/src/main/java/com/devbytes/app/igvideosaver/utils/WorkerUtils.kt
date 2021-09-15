package com.devbytes.app.igvideosaver.utils

import android.util.Log
import androidx.annotation.WorkerThread

import org.jsoup.Jsoup

private const val TAG = "WorkerUtils"

@WorkerThread
fun getInstagramVideoLink(link: String?): String {

    val link=  Jsoup.connect(link).get().select("meta[property=og:video]").attr("content")

    Log.d(TAG, "getInstagramVideoLink: $link")
    return "https://instagram.fjnb11-1.fna.fbcdn.net/v/t50.2886-16/241697000_830105694372108_1255292361976749063_n.mp4?_nc_ht=instagram.fjnb11-1.fna.fbcdn.net&_nc_cat=100&_nc_ohc=diqE46dW8mgAX_Hn_cg&edm=AABBvjUBAAAA&ccb=7-4&oe=6141E1EC&oh=bc28701afc00b3cd151b180eb3c9eee8&_nc_sid=83d603"
}