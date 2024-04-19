package com.example.localdatabaseproject.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.localdatabaseproject.models.ProductList
import com.example.localdatabaseproject.models.User


@Database(entities = [User::class,ProductList::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao():UserDao

}