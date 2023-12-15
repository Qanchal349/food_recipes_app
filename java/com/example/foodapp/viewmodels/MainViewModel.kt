package com.example.foodapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foodapp.data.Repository
import com.example.foodapp.data.database.entities.FavoritesEntity
import com.example.foodapp.data.database.entities.FoodJokeEntity
import com.example.foodapp.data.database.entities.RecipesEntity
import com.example.foodapp.data.network.FoodRecipesApi
import com.example.foodapp.models.FoodJoke
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.util.NetworkResult
import com.example.foodapp.util.RecipesDiffUtil
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import java.security.AccessController.getContext

class MainViewModel @ViewModelInject constructor(
     application: Application,private val repository: Repository
):AndroidViewModel(application) {


    /** ROOM DATABASE OFFLINE CACHING */

    val readRecipesDatabase : LiveData<List<RecipesEntity>> = repository.local.getRecipes().asLiveData()
    val readFavoriteRecipes : LiveData<List<FavoritesEntity>> = repository.local.readFavRecipes().asLiveData()
    val readFoodJoke : LiveData<List<FoodJoke>> = repository.local.readFoodJoke().asLiveData()


    private fun insertFoodJoke(foodJoke: FoodJoke)=viewModelScope.launch {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        repository.local.insertFoodJoke(foodJokeEntity)
    }

   private fun insertRecipes(recipesEntity: RecipesEntity)=viewModelScope.launch {
         repository.local.insertRecipes(recipesEntity)

    }

   fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity)=viewModelScope.launch {
       repository.local.insertFavoriteRecipe(favoritesEntity)
   }

   fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)=viewModelScope.launch {
       repository.local.deleteFavRecipes(favoritesEntity)
   }

   fun deleteAllFavoriteRecipes()=viewModelScope.launch {
       repository.local.deleteAllFavRecipes()
   }



 /**  RETROFIT */

     var readRecipes :MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
     var readSearchRecipes:MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
     var foodJokeResponse:MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.M)
     fun getRecipes(queries :Map<String,String>)=viewModelScope.launch {
        getRecipesSafeCall(queries)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun searchRecipes(searchQuery:Map<String,String>){
        viewModelScope.launch {
            searchRecipesSafeCall(searchQuery)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getFoodJoke(apiKey:String)=viewModelScope.launch {
         getFoodJokeSafeCall(apiKey)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getFoodJokeSafeCall(apiKey: String) {
           foodJokeResponse.value=NetworkResult.Loading()
           if(hasInternetConnection()){
               try {

                   val response = repository.remote.getFoodJoke(apiKey)
                   foodJokeResponse.value=handleFoodJokeResponse(response)
                   val foodJoke = foodJokeResponse.value!!.data
                   if(foodJoke!=null){
                       insertFoodJoke(foodJoke)
                   }
               } catch (e:Exception){
                   foodJokeResponse.value=NetworkResult.Error("No Food Joke found")
               }
           }else{
               foodJokeResponse.value=NetworkResult.Error("No Internet Connection")
           }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke> {
        when{
            response.message().toString().contains("timeout")->{

                return NetworkResult.Error("Timeout")
            }
            response.code()==402->{
                Toast.makeText(getApplication(), "Api limited", Toast.LENGTH_SHORT).show()
                return NetworkResult.Error("Api key Limited")
            }
            response.isSuccessful->{
                return NetworkResult.Success(response.body()!!)
            }
            else->{
                return NetworkResult.Error(response.message())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
      readSearchRecipes.value = NetworkResult.Loading()
        if(hasInternetConnection()){
             try {
                 val response = repository.remote.searchRecipes(searchQuery)
                 readSearchRecipes.value = handleRecipesResponse(response)
             }catch (e:Exception){
                 readSearchRecipes.value=NetworkResult.Error("Recipes Not Found")
             }
        }else{
            readSearchRecipes.value = NetworkResult.Error("No Internet Connection")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
     private suspend  fun getRecipesSafeCall(queries: Map<String, String>) {
       readRecipes.value=NetworkResult.Loading() // only contain Loading class instance while readRecipes not contain another class object like(Success)
         if(hasInternetConnection()){
             try {

                val response = repository.remote.getRecipes(queries)
                 readRecipes.value=handleRecipesResponse(response)
                 val data = readRecipes.value!!.data
                  if(data!=null){
                      // cache data
                      insertRecipes(RecipesEntity(data))
                  }
             }catch (e:Exception){
                 readRecipes.value=NetworkResult.Error("Recipes not Found")
             }

         }else{
             readRecipes.value=NetworkResult.Error("No Internet Connection")
         }


     }

    private fun handleRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
                   when{
                      response.message().toString().contains("timeout")->{

                         return NetworkResult.Error("Timeout")
                      }
                       response.code()==402->{
                           Toast.makeText(getApplication(), "Api limited", Toast.LENGTH_SHORT).show()
                           return NetworkResult.Error("Api key Limited")
                       }
                       response.body()!!.results.isNullOrEmpty()->{
                           return NetworkResult.Error("Recipes not found")
                       }
                       response.isSuccessful->{
                           return NetworkResult.Success(response.body()!!)
                       }
                       else->{
                           return NetworkResult.Error(response.message())
                       }
                  }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =  getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capbilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capbilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
            capbilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)-> true
            capbilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
            else -> false
        }
    }

}