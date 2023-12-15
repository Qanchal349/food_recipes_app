package com.example.foodapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.IngredientAdapter
import com.example.foodapp.models.Result
import kotlinx.android.synthetic.main.fragment_ingredients.view.*


class IngredientsFragment : Fragment() {

    private val mAdapter by lazy { IngredientAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val args = arguments
        val myBundle : Result? = args?.getParcelable("recipesBundle")
        setupRecyclerView(view)
        myBundle?.extendedIngredients?.let { mAdapter.setList(it) }
        return view

    }


    fun setupRecyclerView(view:View){
        view.ingredient.layoutManager=LinearLayoutManager(requireContext())
        view.ingredient.adapter=mAdapter
    }

}