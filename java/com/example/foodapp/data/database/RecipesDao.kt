package com.example.foodapp.data.database

import androidx.room.*
import com.example.foodapp.data.database.entities.FavoritesEntity
import com.example.foodapp.data.database.entities.FoodJokeEntity
import com.example.foodapp.data.database.entities.RecipesEntity
import com.example.foodapp.models.FoodJoke
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id  ASC")
     fun readRecipes(): Flow<List<RecipesEntity>>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity)

     @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
     fun readFavoriteRecipes():Flow<List<FavoritesEntity>>

     @Delete
    suspend fun deleteFavRecipe(favoritesEntity: FavoritesEntity)

     @Query("DELETE FROM favorite_recipes_table")
     suspend fun deleteAllFavRecipes()

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

     @Query("SELECT * FROM food_joke_table")
     fun readFoodJoke():Flow<List<FoodJoke>>



}