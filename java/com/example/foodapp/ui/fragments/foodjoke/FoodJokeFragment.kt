package com.example.foodapp.ui.fragments.foodjoke

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentFoodJokeBinding
import com.example.foodapp.util.Constants.Companion.API_KEY
import com.example.foodapp.util.NetworkResult
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private  var _binding : FragmentFoodJokeBinding?=null
    private lateinit var mainViewModel: MainViewModel
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFoodJokeBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        binding.mainViewModel=mainViewModel
        binding.lifecycleOwner=this
        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner,{response->
            when(response){
                is NetworkResult.Success->{
                    binding.progress.visibility=View.INVISIBLE
                    binding.foodJokeTextView.text=response.data?.text
                    binding.foodJokeTextView.setPadding(10)
                }
                is NetworkResult.Error->{
                    Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
                    loadDataFromCache()
                }
                is NetworkResult.Loading->{
                    binding.progress.visibility=View.VISIBLE
                }
            }
        })

        return binding.root

    }

     private fun loadDataFromCache(){
         lifecycleScope.launch {
             mainViewModel.readFoodJoke.observe(viewLifecycleOwner,{
                 if(it.isNotEmpty() && it!=null)
                     binding.foodJokeTextView.text=it[0].text
                     binding.foodJokeTextView.setPadding(10)
             })
         }
     }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}