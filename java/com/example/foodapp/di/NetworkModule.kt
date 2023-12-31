package com.example.foodapp.di

import com.example.foodapp.data.network.FoodRecipesApi
import com.example.foodapp.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {



    @Provides
    @Singleton
   fun provideHttpClient():OkHttpClient{
       return OkHttpClient.Builder()
           .readTimeout(15,TimeUnit.SECONDS)
           .connectTimeout(15,TimeUnit.SECONDS)
           .build()
   }

    @Singleton
    @Provides
    fun provideGsonConvertFactory():GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient,gsonConverterFactory: GsonConverterFactory):Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit):FoodRecipesApi{
     return retrofit.create(FoodRecipesApi::class.java)
    }

}