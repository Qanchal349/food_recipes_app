package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.data.database.entities.FavoritesEntity
import com.example.foodapp.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foodapp.util.RecipesDiffUtil

class FavoriteRecipesAdapter : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {

    var favoriteList = emptyList<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private lateinit var rootView: View

    class MyViewHolder(private val binding:FavoriteRecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteList[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int =favoriteList.size


    fun setData(newFavoriteList:List<FavoritesEntity>){
        val favoriteRecipesDiffUtil =
            RecipesDiffUtil(favoriteList, newFavoriteList)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteList = newFavoriteList
        diffUtilResult.dispatchUpdatesTo(this)

    }

}