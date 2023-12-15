package com.example.foodapp.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.example.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodapp.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.foodapp.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.foodapp.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.foodapp.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.foodapp.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.foodapp.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context : Context)  {

      private object PreferencesKeys{
       val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
       val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
       val selectedMealTypeId= preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
       val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
       val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)
      }

     private val dataStore:DataStore<Preferences> = context.createDataStore(
         name = PREFERENCES_NAME
     )


     suspend fun saveBackOnline(backOnline:Boolean){
         dataStore.edit {
             it[PreferencesKeys.backOnline]=backOnline
         }
     }

     suspend fun saveMealAndDietType(mealType:String,mealTypeId:Int,dietType:String,dietTypeId:Int){
        dataStore.edit { preferences->
            preferences[PreferencesKeys.selectedMealType]=mealType
            preferences[PreferencesKeys.selectedMealTypeId]=mealTypeId
            preferences[PreferencesKeys.selectedDietType]=dietType
            preferences[PreferencesKeys.selectedDietTypeId]=dietTypeId
        }
     }

     val readBackOnline: Flow<Boolean> = dataStore.data
         .catch { exception->
             if(exception is IOException){
                 emit(emptyPreferences())
             }else{
                 throw exception
             }
         }.map {
         val backOnline = it[PreferencesKeys.backOnline] ?: false
             backOnline
         }

     val readMealAndDietType:Flow<MealAndDietType> = dataStore.data.catch { exception->
         if(exception is IOException){
             emit(emptyPreferences())
         }else{
             throw exception
         }
     }.map {
         val selectedMealType = it[PreferencesKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
         val selectedMealTypeId = it[PreferencesKeys.selectedMealTypeId] ?:0
         val selectedDietType = it[PreferencesKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
         val selectedDietTypeId = it[PreferencesKeys.selectedDietTypeId] ?: 0

         val mealAndDietTye = MealAndDietType(selectedMealType,selectedMealTypeId,selectedDietType,selectedDietTypeId)
         mealAndDietTye
     }

}


 data class MealAndDietType(
   val selectedMealType:String,
   val selectedMealTypeId:Int,
   val selectedDietTye:String,
   val selectedDietTypeId:Int
)