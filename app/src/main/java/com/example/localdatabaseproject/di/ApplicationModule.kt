package com.example.localdatabaseproject.di

import android.content.Context
import androidx.room.Room
import com.example.localdatabaseproject.network.MyApiService
import com.example.localdatabaseproject.roomdb.AppDatabase
import com.example.localdatabaseproject.roomdb.UserDao
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "user_db")
            .allowMainThreadQueries().build()

    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            registerModule(
                KotlinModule.Builder().withReflectionCacheSize(512)
                    .configure(KotlinFeature.NullToEmptyCollection, false)
                    .configure(KotlinFeature.NullToEmptyMap, false)
                    .configure(KotlinFeature.NullIsSameAsDefault, false)
                    .configure(KotlinFeature.SingletonSupport, false)
                    .configure(KotlinFeature.StrictNullChecks, false).build()
            )
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) // Handle empty strings
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val requestWithHeaders: Request = originalRequest.newBuilder()
                    .header("x-rapidapi-host", ApiConstant.API_HOST) // Replace with your token
                    .header("x-rapidapi-key", ApiConstant.API_KEY) // Add your custom headers here
                    .build()
                chain.proceed(requestWithHeaders)
            }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, objectMapper: ObjectMapper
    ): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(ApiConstant.INSTAGRAM_BASE_URL) // Replace with your base URL
            .addConverterFactory(JacksonConverterFactory.create(objectMapper)).build()
    }

    @Provides
    @Singleton
    fun provideMyApiService(retrofit: Retrofit): MyApiService {
        return retrofit.create(MyApiService::class.java)
    }

}