package com.example.foodapp.ui.fragments.recipes

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adapters.RecipesAdapter
import com.example.foodapp.databinding.FragmentRecipesBinding
import com.example.foodapp.util.NetworkListener
import com.example.foodapp.util.NetworkResult
import com.example.foodapp.util.observeOnce
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/**  favorite Adapter not work ,contextual action mode,dark theme */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment() , SearchView.OnQueryTextListener {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var networkListener: NetworkListener
    private val args by navArgs<RecipesFragmentArgs>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        setHasOptionsMenu(true)
        setUpRecyclerView()
        binding.mainViewModel=mainViewModel
        binding.lifecycleOwner=this

        // read backOnline
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner,{
             recipesViewModel.backOnline=it
        })

        //  read database or api(if database is empty)
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipesViewModel.networkState = status
                    recipesViewModel.showNetworkState()
                    readDatabase()
             }
          }

        // set fab button

         binding.floatingActionButton.setOnClickListener {
             if(recipesViewModel.networkState){
                 findNavController().navigate(R.id.action_recipesFragment_to_recipeBottomSheetFragment)
             }else{
                 recipesViewModel.showNetworkState()
             }
         }

           return binding.root
        }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.searchRecipes)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun searchApiData(query: String) {
         showShimmerEffect()
         mainViewModel.searchRecipes(recipesViewModel.searchQuery(query))
         mainViewModel.readSearchRecipes.observe(viewLifecycleOwner,{response->
             when(response){
                 is NetworkResult.Success->{
                     hideShimmerEffect()
                     response.data?.let { mAdapter.setData(it) }
                 }
                 is NetworkResult.Error->{
                     hideShimmerEffect()
                     loadDataFormCache()
                     Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                 }
                 is NetworkResult.Loading->{
                     showShimmerEffect()
                 }
             }
         })

    }




    @RequiresApi(Build.VERSION_CODES.M)
    private fun readDatabase() {
        mainViewModel.readRecipesDatabase.observeOnce(viewLifecycleOwner,{database->
            if(database.isNotEmpty() && !args.backFromBottomSheet){
                   mAdapter.setData(database[0].recipes)
                   hideShimmerEffect()
            }else{
                   requestApiData()
            }
        })

    }

        @RequiresApi(Build.VERSION_CODES.M)
        private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.readRecipes.observe(viewLifecycleOwner,{response->
          when(response){
              is NetworkResult.Success->{
                  hideShimmerEffect()
                  response.data?.let { mAdapter.setData(it) }
              }
              is NetworkResult.Error->{
                  hideShimmerEffect()
                  Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                  loadDataFormCache()
              }
              is NetworkResult.Loading->{
                  showShimmerEffect()
              }

          }
      })

    }

        private fun loadDataFormCache(){
           lifecycleScope.launch {
              mainViewModel.readRecipesDatabase.observe(viewLifecycleOwner,{database->
                   if(database.isNotEmpty()){
                       mAdapter.setData(database[0].recipes)
                   }
              })
           }
       }

        private fun setUpRecyclerView() {
            binding.apply {
                recyclerview.layoutManager = LinearLayoutManager(requireContext())
                recyclerview.adapter = mAdapter
            }
        }

        private fun showShimmerEffect() {
            binding.recyclerview.showShimmer()
        }

        private fun hideShimmerEffect() {
            binding.recyclerview.hideShimmer()
        }

        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }



}


