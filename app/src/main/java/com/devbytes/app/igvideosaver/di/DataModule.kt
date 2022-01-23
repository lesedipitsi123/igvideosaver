package com.devbytes.app.igvideosaver.di

import android.content.Context
import androidx.room.Room
import com.devbytes.app.igvideosaver.data.dao.InstagramMediaDao
import com.devbytes.app.igvideosaver.data.db.SocialGrabDatabase
import com.devbytes.app.igvideosaver.utils.ConstantsUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    fun provideInstagramMediaDao(db : SocialGrabDatabase) : InstagramMediaDao {
        return db.mediaDao()
    }

    @Provides
    @Singleton
    fun provideSocialGrabDatabase(@ApplicationContext context: Context) : SocialGrabDatabase {
        return Room.databaseBuilder(context, SocialGrabDatabase::class.java, ConstantsUtils.DATABASE_NAME).build()
    }
}