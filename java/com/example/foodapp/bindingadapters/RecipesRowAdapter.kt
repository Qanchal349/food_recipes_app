package com.example.foodapp.bindingadapters


import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foodapp.R
import com.example.foodapp.models.Result
import com.example.foodapp.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup

class RecipesRowAdapter {

    companion object{

        @JvmStatic
        @BindingAdapter("onRecipesClickListener")
        fun onRecipesClickListener(recipesRowLayout:ConstraintLayout,result: Result){
            recipesRowLayout.setOnClickListener {
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailActivity(result)
                    recipesRowLayout.findNavController().navigate(action)
                }catch (e:Exception){
                    Log.d("onRecipeClickListener",e.toString())
                }

            }

        }

        @BindingAdapter("setRecipesImage")
        @JvmStatic
        fun setRecipesImage(imageView: ImageView,imageUri: String){
             imageView.load(imageUri){
                 crossfade(600)
                 error(R.drawable.ic_error)
             }
        }

         @JvmStatic
         @BindingAdapter("setRecipesLikes")
        fun setRecipesLikes(textView: TextView,likes:Int){
           textView.text=likes.toString()
        }

        @JvmStatic
        @BindingAdapter("setRecipesTime")
       fun setRecipesTime(textView: TextView,time:Int){
             textView.text=time.toString()
       }
        @JvmStatic
        @BindingAdapter("setRecipesSummary")
        fun setRecipesSummary(textView: TextView,text:String){
            val summary = Jsoup.parse(text).text()
            textView.text=summary
        }

        @JvmStatic
        @BindingAdapter("applyVeganColor")
       fun applyVeganColor(view: View,vegan:Boolean){
           if(vegan){
               when(view){
                   is TextView->{
                       view.setTextColor(ContextCompat.getColor(view.context,R.color.green))
                   }
                   is ImageView->{
                       view.setColorFilter(ContextCompat.getColor(view.context,R.color.green))
                   }
               }
           }

       }



    }

}