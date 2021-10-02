package com.devbytes.app.igvideosaver

import android.app.Application
import com.devbytes.app.igvideosaver.data.repository.VideoRepository
import com.devbytes.app.igvideosaver.data.room.db.SocialGrabDatabase

class SocialGrabApplication : Application() {
    val database by lazy { SocialGrabDatabase.getDatabase(this) }
    val repository by lazy { VideoRepository(database.videoDao()) }
}