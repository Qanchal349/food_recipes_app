package com.example.foodapp.data.database

import androidx.room.TypeConverter
import com.example.foodapp.data.database.entities.RecipesEntity
import com.example.foodapp.models.FoodJoke
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

      var gson = Gson()


     @TypeConverter  // use to insert data in database
     fun foodRecipesToString(recipes: FoodRecipe):String{
         return gson.toJson(recipes)
     }

    @TypeConverter
    fun stringToFoodRecipes(data:String):FoodRecipe{
       val listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToString(result:Result):String{
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String):Result{
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun foodJokeToString(foodJoke: FoodJoke):String{
        return gson.toJson(foodJoke)
    }

    @TypeConverter
    fun stringToFoodJoke(data:String):FoodJoke{
        val listType = object : TypeToken<FoodJoke>(){}.type
        return gson.fromJson(data,listType)
    }

}