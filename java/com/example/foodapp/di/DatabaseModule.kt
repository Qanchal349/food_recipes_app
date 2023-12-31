package com.example.foodapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodapp.data.database.RecipesDao
import com.example.foodapp.data.database.RecipesDatabase
import com.example.foodapp.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context)= Room.databaseBuilder(
          context,
          RecipesDatabase::class.java,
          DATABASE_NAME
    ).build()


    @Provides
    @Singleton
    fun provideDao(database: RecipesDatabase):RecipesDao{
        return database.recipesDao()
    }

}