package com.example.foodapp.ui.fragments.recipes

import android.app.Application
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foodapp.data.DataStoreRepository
import com.example.foodapp.data.MealAndDietType
import com.example.foodapp.util.Constants.Companion.API_KEY
import com.example.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodapp.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foodapp.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foodapp.util.Constants.Companion.QUERY_API_KEY
import com.example.foodapp.util.Constants.Companion.QUERY_DIET
import com.example.foodapp.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foodapp.util.Constants.Companion.QUERY_NUMBER
import com.example.foodapp.util.Constants.Companion.QUERY_SEARCH
import com.example.foodapp.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(application: Application,
 private val dataStoreRepository: DataStoreRepository
)  : AndroidViewModel(application)  {


    val readBackOnline : LiveData<Boolean> = dataStoreRepository.readBackOnline.asLiveData()
    val readDietAndMealType = dataStoreRepository.readMealAndDietType

    var backOnline=false
    var networkState=false

    private var mealType= DEFAULT_MEAL_TYPE
    private var dietType= DEFAULT_DIET_TYPE


    fun searchQuery(query:String):HashMap<String,String>{
         val queries : HashMap<String,String> = HashMap()
        queries[QUERY_SEARCH] = query
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

         return queries
    }

    fun applyQueries():Map<String,String>{

        viewModelScope.launch {
            readDietAndMealType.collect{
                mealType=it.selectedMealType
                dietType=it.selectedDietTye
            }
        }
        val queries:HashMap<String,String> = HashMap()
        queries[QUERY_NUMBER]=DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY]= API_KEY
        queries[QUERY_TYPE]=mealType
        queries[QUERY_DIET]= dietType
        queries[QUERY_ADD_RECIPE_INFORMATION]="true"
        queries[QUERY_FILL_INGREDIENTS]="true"
        return queries

    }

  fun saveDietAndMealType(mealType:String,mealTypeId:Int,dietType:String,dietTypeId:Int)=viewModelScope.launch {
       dataStoreRepository.saveMealAndDietType(mealType,mealTypeId,dietType,dietTypeId)
  }

    fun saveBackOnline(backOnline:Boolean)=viewModelScope.launch {
      dataStoreRepository.saveBackOnline(backOnline)
  }

  fun showNetworkState(){

      if(!networkState){
          Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
          saveBackOnline(true)
      }else if(networkState){
          if(backOnline){
              Toast.makeText(getApplication(), "Back Online", Toast.LENGTH_SHORT).show()
              saveBackOnline(false)
          }
      }


  }




}