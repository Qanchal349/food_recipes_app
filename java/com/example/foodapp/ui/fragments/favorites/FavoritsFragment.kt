package com.example.foodapp.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.adapters.FavoriteRecipesAdapter
import com.example.foodapp.databinding.FragmentFavoritsBinding
import com.example.foodapp.databinding.FragmentRecipesBinding
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritsFragment : Fragment() {

    private var _binding : FragmentFavoritsBinding?=null
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter() }
    private val binding get() = _binding!!
    private lateinit var mainViewModel:MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentFavoritsBinding.inflate(layoutInflater)
        mainViewModel=ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.mainViewModel=mainViewModel
        binding.lifecycleOwner=this
        binding.mAdapter = mAdapter
        setupRecyclerView(binding.favoriteRecipesRecyclerView)
        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}