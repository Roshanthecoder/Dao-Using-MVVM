package com.example.localdatabaseproject.di

import android.content.Context
import androidx.room.Room
import com.example.localdatabaseproject.repository.RoomRepo
import com.example.localdatabaseproject.roomdb.AppDatabase
import com.example.localdatabaseproject.roomdb.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "user_db").allowMainThreadQueries().build()

    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }



}