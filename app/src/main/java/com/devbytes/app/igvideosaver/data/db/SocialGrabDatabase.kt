package com.devbytes.app.igvideosaver.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devbytes.app.igvideosaver.data.entites.converters.DateConverter
import com.devbytes.app.igvideosaver.data.dao.InstagramMediaDao
import com.devbytes.app.igvideosaver.data.entites.InstagramMedia

@Database(entities = [InstagramMedia::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class SocialGrabDatabase : RoomDatabase() {
    abstract fun mediaDao(): InstagramMediaDao
}