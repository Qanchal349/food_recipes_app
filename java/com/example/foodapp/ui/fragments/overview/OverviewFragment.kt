package com.example.foodapp.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.foodapp.R
import com.example.foodapp.models.Result
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.fragment_overview.view.description_textView
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.fragment_recipes.imageView
import kotlinx.android.synthetic.main.recipes_row_layout.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
         val args = arguments
         val myBundle:Result? = args?.getParcelable("recipesBundle")
         view.imageView3.load(myBundle?.image){
             error(R.drawable.ic_error)
         }
         view.description_textView.text=myBundle?.title
         view.textView3.text=myBundle?.aggregateLikes.toString()
         view.textView4.text=myBundle?.readyInMinutes.toString()
         myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.summary_textView.text=summary
        }

        if(myBundle?.cheap==true){
            setColor(view.cheap_textView,view.cheap_imageView)
        }
        if(myBundle?.vegetarian==true){
            setColor(view.vegetraian_textView,view.vegetraian_imageView)
        }
        if(myBundle?.veryHealthy==true){
            setColor(view.healthy_textView,view.healthy_imageView)
        }
       if(myBundle?.vegan==true){
           setColor(view.vegan_textView,view.vegan_imageView)
       }
        if(myBundle?.dairyFree==true){
            setColor(view.dairy_free_textView,view.dairy_free_imageView)
        }
        if(myBundle?.glutenFree==true){
            setColor(view.gluten_free_textView,view.gluten_free_imageView)
        }


        return view

    }

    private fun setColor(cheapTextview: TextView?, cheapImageview: ImageView?) {
        cheapImageview?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        cheapTextview?.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }


}