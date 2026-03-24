package com.example.fpquiz.di

import android.content.Context
import androidx.room.Room
import com.example.fpquiz.data.local.AppDatabase
import com.example.fpquiz.data.local.PartidaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.fpquiz.data.remote.QuizApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://mocki.io/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient.Builder().build())
            .build()
    }

    @Provides
    @Singleton
    fun provideQuizApiService(retrofit: Retrofit): QuizApiService =
        retrofit.create(QuizApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "fpquiz.db")
            .build()

    @Provides
    fun providePartidaDao(db: AppDatabase): PartidaDao = db.partidaDao()
}
