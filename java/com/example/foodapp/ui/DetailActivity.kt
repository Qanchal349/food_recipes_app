package com.example.foodapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.foodapp.R
import com.example.foodapp.adapters.PagerAdapter
import com.example.foodapp.data.database.entities.FavoritesEntity
import com.example.foodapp.ui.fragments.ingredients.IngredientsFragment
import com.example.foodapp.ui.fragments.instructions.InstructionFragment
import com.example.foodapp.ui.fragments.overview.OverviewFragment
import com.example.foodapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_overview.*
import java.lang.Exception

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val args by navArgs<DetailActivityArgs>()
    private var recipeSaved = false
    private var savedRecipeId = 0
    private lateinit var mainViewModel:MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instruction")

        val resultBundle = Bundle().apply {
            putParcelable("recipesBundle",args.result)
        }

        val adapter = PagerAdapter(resultBundle,fragments,titles,supportFragmentManager)
        viewPager.adapter=adapter
        tabLayout.setupWithViewPager(viewPager)




    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish() // this will close our detail activity
        }else if(item.itemId==R.id.save_to_favorites_menu && !recipeSaved){
              saveRecipe(item)
        }
        else if(item.itemId==R.id.save_to_favorites_menu && recipeSaved){
            removeRecipe(item)
        }
        return super.onOptionsItemSelected(item)
    }




    private fun removeRecipe(item: MenuItem) {
        val favoritesEntity =
            FavoritesEntity(
                savedRecipeId,
                args.result
            )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show()
        recipeSaved = false
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }

    private fun saveRecipe(item: MenuItem) {
        val favoritesEntity =
            FavoritesEntity(
                0,
                args.result
            )
        mainViewModel.insertFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show()
        recipeSaved = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu,menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        if (menuItem != null) {
            checkedSaveRecipes(menuItem)
        }
        return true
    }

    private fun checkedSaveRecipes(menuItem: MenuItem) {
          try {
              mainViewModel.readFavoriteRecipes.observe(this,{favorites->
                  for (savedRecipe in favorites){
                      if(savedRecipe.id ==args.result.id){
                          recipeSaved=true
                          savedRecipeId=args.result.id
                          changeMenuItemColor(menuItem,R.color.yellow)
                      }else{
                          changeMenuItemColor(menuItem,R.color.white)
                      }
                  }
              })


          }catch (e:Exception){
              Log.d("DetailActivity" , e.message.toString())
          }
    }


}