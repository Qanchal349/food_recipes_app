package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodapp.R
import com.example.foodapp.models.ExtendedIngredient
import com.example.foodapp.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foodapp.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*
import java.util.*

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.imageView6.load(BASE_IMAGE_URL + ingredientsList[position].image){
            error(R.drawable.ic_error)
        }
        holder.itemView.textView2.text=ingredientsList[position].name.capitalize(Locale.ROOT)
        holder.itemView.textView5.text =ingredientsList[position].amount.toString()
        holder.itemView.textView6.text =ingredientsList[position].unit
        holder.itemView.textView7.text =ingredientsList[position].consistency
        holder.itemView.textView8.text =ingredientsList[position].original

    }

    override fun getItemCount(): Int = ingredientsList.size

    fun setList(newIngredients:List<ExtendedIngredient>){
        val ingredientsDiffUtil =
            RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}