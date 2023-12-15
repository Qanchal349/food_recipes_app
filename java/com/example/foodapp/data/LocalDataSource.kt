package com.example.foodapp.data

import com.example.foodapp.data.database.RecipesDao
import com.example.foodapp.data.database.entities.FavoritesEntity
import com.example.foodapp.data.database.entities.FoodJokeEntity
import com.example.foodapp.data.database.entities.RecipesEntity
import com.example.foodapp.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val foodRecipesDao: RecipesDao) {

      fun getRecipes(): Flow<List<RecipesEntity>> {
          return foodRecipesDao.readRecipes()
      }

      suspend fun insertRecipes(recipesEntity: RecipesEntity){
          foodRecipesDao.insertRecipes(recipesEntity)
      }

     suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity){
         foodRecipesDao.insertFavoriteRecipes(favoritesEntity)
     }

     fun readFavRecipes():Flow<List<FavoritesEntity>>{
         return foodRecipesDao.readFavoriteRecipes()
     }

     suspend fun deleteFavRecipes(favoritesEntity: FavoritesEntity){
         foodRecipesDao.deleteFavRecipe(favoritesEntity)
     }

    suspend fun deleteAllFavRecipes(){
        foodRecipesDao.deleteAllFavRecipes()
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        foodRecipesDao.insertFoodJoke(foodJokeEntity)
    }

    fun readFoodJoke():Flow<List<FoodJoke>>{
        return foodRecipesDao.readFoodJoke()
    }
}