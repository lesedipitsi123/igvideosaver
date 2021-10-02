package com.devbytes.app.igvideosaver.data.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devbytes.app.igvideosaver.data.entites.VideoEntity
import com.devbytes.app.igvideosaver.data.entites.converters.DateConverter
import com.devbytes.app.igvideosaver.data.room.dao.VideoDao

@Database(entities = [VideoEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class SocialGrabDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: SocialGrabDatabase? = null

        fun getDatabase(context: Context): SocialGrabDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocialGrabDatabase::class.java,
                    "social_grab_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}