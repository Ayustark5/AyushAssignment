package com.ayustark.ayushassignment.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.ayustark.ayushassignment.database.SwaadDatabase
import com.ayustark.ayushassignment.network.ApiService
import com.ayustark.ayushassignment.repositories.Repository
import com.ayustark.ayushassignment.repositories.RepositoryImpl
import com.ayustark.ayushassignment.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun providesFloApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesRepository(
        api: ApiService,
        sharedPreferences: SharedPreferences,
        swaadDatabase: SwaadDatabase
    ): Repository = RepositoryImpl(api, sharedPreferences, swaadDatabase)

    @Singleton
    @Provides
    fun providesDb(@ApplicationContext context: Context): SwaadDatabase =
        Room.databaseBuilder(context, SwaadDatabase::class.java, "swaad_db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = EncryptedSharedPreferences.create(
        "user",
        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}