package com.example.foodapp.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.provider.UserDictionary.Words.LOCALE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.ui.fragments.recipes.RecipesViewModel
import com.example.foodapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_recipe_bottom_sheet.view.*
import java.lang.Exception
import java.util.*


class RecipeBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var mView:View
    lateinit var recipesViewModel: RecipesViewModel
    private var mealTypeChip=DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip=DEFAULT_DIET_TYPE
    private var dietTypeChipId=0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.fragment_recipe_bottom_sheet, container, false)
         recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

         recipesViewModel.readDietAndMealType.asLiveData().observe(viewLifecycleOwner,{
                mealTypeChip=it.selectedMealType
                dietTypeChip=it.selectedDietTye
               updateSelectedChip(it.selectedMealTypeId,mView.mealType_chipGroup)
               updateSelectedChip(it.selectedDietTypeId,mView.dietType_chipGroup)
         })

        mView.mealType_chipGroup.setOnCheckedChangeListener { group, checkedIds ->
               val chip = group.findViewById<Chip>(checkedIds)
               val selectedMealChipText=chip.text.toString().lowercase(Locale.ROOT)
                mealTypeChip=selectedMealChipText
                mealTypeChipId=checkedIds
          }

         mView.dietType_chipGroup.setOnCheckedChangeListener { group, checkedId ->
             val chip = group.findViewById<Chip>(checkedId)
             val selectedDietChipText=chip.text.toString().lowercase(Locale.ROOT)
             dietTypeChip=selectedDietChipText
             dietTypeChipId=checkedId
         }

          mView.apply_btn.setOnClickListener {
              recipesViewModel.saveDietAndMealType(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)
              val action = RecipeBottomSheetFragmentDirections.actionRecipeBottomSheetFragmentToRecipesFragment(true)
              findNavController().navigate(action)
          }

         return mView
    }

    private fun updateSelectedChip(chipId: Int, ChipGroup: ChipGroup?) {
         if(chipId!=0){
             try {
                 ChipGroup?.findViewById<Chip>(chipId)?.isChecked = true
             }catch (e:Exception){
                 Log.d("RecipesBottomSheet",e.message.toString())
             }
         }
    }


}