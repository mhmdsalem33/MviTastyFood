package com.example.mvitastyfood.di

import android.app.Application
import androidx.room.Room
import com.example.mvitastyfood.data.db.MealDatabase
import com.example.mvitastyfood.data.remote.MealApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideApi() :MealApi =
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApi::class.java)

    @Singleton
    @Provides
    fun provideDatabase(app : Application) : MealDatabase =
        Room.databaseBuilder(app , MealDatabase::class.java , "meal.db").build()




}