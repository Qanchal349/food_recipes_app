package com.example.foodapp.data.network

import com.example.foodapp.models.FoodJoke
import com.example.foodapp.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

// we need use DI to make the instance of FoodRecipeInterface using retrofit

interface FoodRecipesApi {

    // BASE URL + /recipes/complexSearch/queries
    @GET("/recipes/complexSearch") // Apikey include in query map
    suspend fun getRecipes(@QueryMap queries : Map<String,String>): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(@QueryMap searchQuery:Map<String,String> ):Response<FoodRecipe>



    // Food Joke
    @GET("food/jokes/random")
    suspend fun getFoodJoke(@Query("apiKey") apiKey:String):Response<FoodJoke>




}