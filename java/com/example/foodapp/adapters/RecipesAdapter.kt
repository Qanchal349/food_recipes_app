package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.RecipesRowLayoutBinding
import com.example.foodapp.models.FoodRecipe
import com.example.foodapp.models.Result
import com.example.foodapp.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyRecipesAdapter>() {

    private var recipes= emptyList<Result>()

    class MyRecipesAdapter(private val binding:RecipesRowLayoutBinding):RecyclerView.ViewHolder(binding.root) {
               fun bind(result: Result){
                   binding.result=result
                   binding.executePendingBindings()
               }

         companion object{
              fun from(parent:ViewGroup):MyRecipesAdapter{
                 val layoutInflater = LayoutInflater.from(parent.context)
                  val binding = RecipesRowLayoutBinding.inflate(layoutInflater,parent,false)
                  return MyRecipesAdapter(binding)
              }
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesAdapter {
        return MyRecipesAdapter.from(parent)
    }

    override fun onBindViewHolder(holder: MyRecipesAdapter, position: Int) {
        val currentRecipe = recipes[position]
         holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int = recipes.size

    fun setData(newData: FoodRecipe){
        val recipesDiffUtil = RecipesDiffUtil(recipes,newData.results)
        val diffUtilResult= DiffUtil.calculateDiff(recipesDiffUtil)
        recipes=newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }

}

