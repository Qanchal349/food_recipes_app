package com.example.foodapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.adapters.FavoriteRecipesAdapter
import com.example.foodapp.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {

     companion object{


         @JvmStatic
         @BindingAdapter("viewVisibility", "setData", requireAll = false)
         fun setDataAndViewVisibility(view: View,favoritesEntity:List<FavoritesEntity>?,mAdapter:FavoriteRecipesAdapter?){
               if(favoritesEntity.isNullOrEmpty()){
                   when(view){
                       is ImageView->{
                           view.visibility=View.VISIBLE
                       }
                       is TextView->{
                           view.visibility=View.VISIBLE
                       }
                       is RecyclerView->{
                           view.visibility=View.INVISIBLE
                       }
                   }

               }else{
                   when(view){
                       is TextView->{
                           view.visibility=View.INVISIBLE
                       }
                       is ImageView->{
                           view.visibility=View.INVISIBLE
                       }
                       is RecyclerView->{
                           view.visibility=View.VISIBLE
                           mAdapter?.setData(favoritesEntity)
                       }
                   }
               }
         }


     }

}